import os
import warnings
import joblib

import numpy as np
import pandas as pd
import matplotlib
matplotlib.use("Agg")           # pentru salvare PNG
import matplotlib.pyplot as plt

from sklearn.model_selection import train_test_split, StratifiedKFold, cross_validate
from sklearn.preprocessing   import StandardScaler
from sklearn.linear_model    import LogisticRegression
from sklearn.tree            import DecisionTreeClassifier
from sklearn.ensemble        import RandomForestClassifier
from sklearn.metrics         import (
    accuracy_score, precision_score, recall_score,
    f1_score, roc_auc_score, confusion_matrix, roc_curve,
    ConfusionMatrixDisplay
)
from imblearn.over_sampling import SMOTE

warnings.filterwarnings("ignore")   # suprima avertismentele de convergenta LR


DATASET_PATH   = "dataset/PIDD.csv"
MODELS_DIR     = "models"
RESULTS_DIR    = "results"
RANDOM_STATE   = 42          # seed pentru reproductibilitate
TEST_SIZE      = 0.20        # 80% train / 20% test
CV_FOLDS       = 5           # numarul de pliuri pentru validarea incrucisata

BIOLOGICAL_ZERO_COLS = ["Glucose", "BloodPressure", "SkinThickness", "Insulin", "BMI"]

FEATURE_NAMES = ["Glucose", "BloodPressure", "SkinThickness",
                 "Insulin", "BMI", "DiabetesPedigreeFunction", "Age"]

os.makedirs(MODELS_DIR,  exist_ok=True)
os.makedirs(RESULTS_DIR, exist_ok=True)



def load_data(path: str) -> pd.DataFrame:
    """
    Motivatie eliminare `Pregnancies`:
        Setul PIDD contine exclusiv date de la femei, insa modelul final
        trebuie sa fie generalizabil ambelor sexe. Pastrarea unui feature
        specific unui singur sex ar introduce un bias structural — pentru
        un pacient de sex masculin valoarea ar fi intotdeauna 0, afectand
        calitatea predictiei.

    """
    df = pd.read_csv(path)
    print(f"[1] Date incarcate: {df.shape[0]} inregistrari, {df.shape[1]} coloane")

    df = df.drop(columns=["Pregnancies"])
    print(f"    Coloana 'Pregnancies' eliminata. Coloane ramase: {list(df.columns)}")
    return df


def split_data(df: pd.DataFrame):
    X = df.drop(columns=["Outcome"]).values
    y = df["Outcome"].values

    X_train, X_test, y_train, y_test = train_test_split(
        X, y,
        test_size    = TEST_SIZE,
        random_state = RANDOM_STATE,
        stratify     = y          # pastreaza distributia claselor
    )

    print(f"[2] Split: train={X_train.shape[0]} | test={X_test.shape[0]}")
    print(f"    Distributie train — 0:{(y_train==0).sum()} | 1:{(y_train==1).sum()}")
    print(f"    Distributie test  — 0:{(y_test==0).sum()}  | 1:{(y_test==1).sum()}")
    return X_train, X_test, y_train, y_test


def mark_biological_zeros(df: pd.DataFrame) -> pd.DataFrame:
    """
    Inlocuieste zerourile biologice imposibile cu NaN.
    Coloane afectate: Glucose, BloodPressure, SkinThickness, Insulin, BMI
    """
    df = df.copy()
    for col in BIOLOGICAL_ZERO_COLS:
        df[col] = df[col].replace(0, np.nan)
    return df


def impute_with_train_median(X_train: np.ndarray, X_test: np.ndarray) -> tuple:
    X_train = X_train.copy().astype(float)
    X_test  = X_test.copy().astype(float)

    print("[3] Imputare cu mediana din train:")
    for i, col_name in enumerate(FEATURE_NAMES):
        if col_name not in BIOLOGICAL_ZERO_COLS:
            continue

        train_nan = np.isnan(X_train[:, i]).sum()
        test_nan  = np.isnan(X_test[:, i]).sum()

        if train_nan == 0 and test_nan == 0:
            continue

        # Mediana calculata DOAR pe valorile non-NaN din train
        median_val = np.nanmedian(X_train[:, i])

        X_train[np.isnan(X_train[:, i]), i] = median_val
        X_test [np.isnan(X_test[:, i]),  i] = median_val

        print(f"    [{col_name}] median_train={median_val:.2f} | "
              f"NaN imputate: train={train_nan}, test={test_nan}")

    print(f"    NaN ramase dupa imputare: "
          f"train={np.isnan(X_train).sum()}, test={np.isnan(X_test).sum()}")
    return X_train, X_test


# ─────────────────────────────────────────────────────────────────────────────
# 5.  SCALARE + SMOTE
# ─────────────────────────────────────────────────────────────────────────────

def scale_and_smote(X_train, X_test, y_train):
    """
    Aplica StandardScaler si SMOTE exclusiv pe setul de antrenare.

    Ordinea operatiilor este CRITICA pentru a evita data leakage:
        1. Fit scaler DOAR pe X_train  ->  transform X_train si X_test
        2. Aplica SMOTE DUPA scalare,  DOAR pe X_train scalat

    De ce StandardScaler?
        Algoritmii sensibili la scara (ex: Logistic Regression) necesita
        features pe aceeasi scala. Chiar si arborii beneficiaza indirect.

    De ce SMOTE?
        Setul PIDD este dezechilibrat (~65% clasa 0 vs ~35% clasa 1).
        SMOTE genereaza instante sintetice ale clasei minoritare prin
        interpolare intre vecini apropiati — fara a elimina date reale,
        spre deosebire de undersampling.

    Returns
    -------
    X_train_res, y_train_res  — date de antrenare echilibrate si scalate
    X_test_scaled             — date de test scalate (fara SMOTE!)
    scaler                    — obiectul StandardScaler (salvat mai tarziu)
    """
    # ── Scalare ──────────────────────────────────────────────────────────────
    scaler        = StandardScaler()
    X_train_sc    = scaler.fit_transform(X_train)   # fit + transform pe train
    X_test_scaled = scaler.transform(X_test)        # doar transform pe test

    print(f"[4a] Scalare aplicata (StandardScaler fit pe train).")

    # ── SMOTE ─────────────────────────────────────────────────────────────────
    smote = SMOTE(random_state=RANDOM_STATE)
    X_train_res, y_train_res = smote.fit_resample(X_train_sc, y_train)

    print(f"[4b] SMOTE aplicat pe train:")
    print(f"     Inainte: 0={(y_train==0).sum()} | 1={(y_train==1).sum()}")
    print(f"     Dupa:    0={(y_train_res==0).sum()} | 1={(y_train_res==1).sum()}")

    return X_train_res, y_train_res, X_test_scaled, scaler


# ─────────────────────────────────────────────────────────────────────────────
# 6.  DEFINIRE MODELE
# ─────────────────────────────────────────────────────────────────────────────

def get_models() -> dict:
    """
    Returneaza dictionarul de modele cu hiperparametrii de baza.

    Logistic Regression
        Model liniar de referinta (baseline). Calculeaza probabilitatea
        de apartententa la clasa 1 prin functia sigmoida aplicata pe o
        combinatie liniara a features. max_iter=1000 asigura convergenta
        pe datele scalate.

    Decision Tree (max_depth=5)
        Model interpretabil: genereaza reguli de tip
        "Daca Glucoza > 125 si BMI > 30 -> Risc Ridicat".
        Adancimea limitata la 5 previne overfitting-ul.

    Random Forest (n_estimators=100)
        Ansamblu de 100 de arbori antrenati pe subseturi aleatorii (bagging).
        Prin agregarea voturilor reduce variatia si corecteaza tendinta
        arborilor individuali de a se supraspecializa pe datele de antrenare.
    """
    return {
        "Logistic Regression": LogisticRegression(
            max_iter     = 1000,
            random_state = RANDOM_STATE
        ),
        "Decision Tree": DecisionTreeClassifier(
            max_depth    = 5,
            random_state = RANDOM_STATE
        ),
        "Random Forest": RandomForestClassifier(
            n_estimators = 100,
            random_state = RANDOM_STATE,
            n_jobs       = -1      # foloseste toate nucleele disponibile
        ),
    }


# ─────────────────────────────────────────────────────────────────────────────
# 7.  ANTRENARE + EVALUARE PE TEST SET
# ─────────────────────────────────────────────────────────────────────────────

def train_and_evaluate(models, X_train, y_train, X_test, y_test) -> pd.DataFrame:
    """
    Antreneaza fiecare model si calculeaza metricile pe setul de test.

    Metrici calculate
    -----------------
    Accuracy  : (TP+TN) / N         — proportia predictiilor corecte
    Precision : TP / (TP+FP)        — din cei prezisi pozitiv, cati sunt real pozitivi
    Recall    : TP / (TP+FN)        — din toti pozitivii reali, cati au fost detectati
    F1        : 2*(P*R)/(P+R)       — media armonica intre Precision si Recall
    ROC-AUC   : aria sub curba ROC  — capacitatea de discriminare pe orice prag

    In contextul medical, Recall (sensibilitatea) este critica: un False
    Negative (pacient diabetic prezis sanatos) este mai periculos decat
    un False Positive.

    Returns
    -------
    pd.DataFrame cu metricile pentru fiecare model
    """
    print("\n[5] Antrenare si evaluare modele:")
    results = []

    for name, model in models.items():
        model.fit(X_train, y_train)

        y_pred  = model.predict(X_test)
        y_proba = model.predict_proba(X_test)[:, 1]   # probabilitate clasa 1

        row = {
            "Model"    : name,
            "Accuracy" : round(accuracy_score (y_test, y_pred),   4),
            "Precision": round(precision_score(y_test, y_pred),   4),
            "Recall"   : round(recall_score   (y_test, y_pred),   4),
            "F1"       : round(f1_score       (y_test, y_pred),   4),
            "ROC-AUC"  : round(roc_auc_score  (y_test, y_proba),  4),
        }
        results.append(row)
        print(f"    {name:22s} | Acc={row['Accuracy']:.4f} | "
              f"F1={row['F1']:.4f} | AUC={row['ROC-AUC']:.4f}")

    df_metrics = pd.DataFrame(results)
    df_metrics.to_csv(os.path.join(RESULTS_DIR, "metrics.csv"), index=False)
    print(f"    Metrici salvate in {RESULTS_DIR}/metrics.csv")
    return df_metrics


# ─────────────────────────────────────────────────────────────────────────────
# 8.  VALIDARE INCRUCISATA (5-FOLD CV)
# ─────────────────────────────────────────────────────────────────────────────

def cross_validate_models(models, X_train, y_train) -> pd.DataFrame:
    """
    Valideaza fiecare model prin 5-fold Stratified Cross-Validation.

    CV se aplica pe setul de antrenare (post-SMOTE) si masoara stabilitatea
    modelului: o diferenta mica intre fold-uri indica un model robust care
    nu depinde de o anumita impartire a datelor.

    Returneaza media si deviatia standard pentru fiecare metrica.
    """
    print("\n[6] Validare incrucisata (5-fold CV pe train set):")
    skf     = StratifiedKFold(n_splits=CV_FOLDS, shuffle=True, random_state=RANDOM_STATE)
    scoring = ["accuracy", "precision", "recall", "f1", "roc_auc"]
    cv_rows = []

    for name, model in models.items():
        scores = cross_validate(model, X_train, y_train, cv=skf, scoring=scoring)
        row = {"Model": name}
        for metric in scoring:
            mean_val = scores[f"test_{metric}"].mean()
            std_val  = scores[f"test_{metric}"].std()
            row[f"{metric}_mean"] = round(mean_val, 4)
            row[f"{metric}_std"]  = round(std_val,  4)
        cv_rows.append(row)
        print(f"    {name:22s} | CV Acc={row['accuracy_mean']:.4f}+/-{row['accuracy_std']:.4f} "
              f"| CV F1={row['f1_mean']:.4f}+/-{row['f1_std']:.4f}")

    df_cv = pd.DataFrame(cv_rows)
    df_cv.to_csv(os.path.join(RESULTS_DIR, "cv_metrics.csv"), index=False)
    print(f"    CV metrici salvate in {RESULTS_DIR}/cv_metrics.csv")
    return df_cv


# ─────────────────────────────────────────────────────────────────────────────
# 9.  GRAFICE
# ─────────────────────────────────────────────────────────────────────────────

def plot_confusion_matrices(models, X_test, y_test):
    """
    Salveaza cate un grafic de confusion matrix pentru fiecare model.

    Confusion Matrix arata distributia TP, FP, TN, FN si permite
    o intelegere vizuala rapida a tipului de erori ale modelului.
    """
    print("\n[7] Generare grafice confusion matrix...")
    for name, model in models.items():
        y_pred = model.predict(X_test)
        cm     = confusion_matrix(y_test, y_pred)

        fig, ax = plt.subplots(figsize=(5, 4))
        disp = ConfusionMatrixDisplay(
            confusion_matrix = cm,
            display_labels   = ["Sanatos (0)", "Diabetic (1)"]
        )
        disp.plot(ax=ax, colorbar=False, cmap="Blues")
        ax.set_title(f"Confusion Matrix — {name}", fontsize=12, pad=12)
        plt.tight_layout()

        fname = name.replace(" ", "_")
        path  = os.path.join(RESULTS_DIR, f"confusion_matrix_{fname}.png")
        plt.savefig(path, dpi=150, bbox_inches="tight")
        plt.close()
        print(f"    Salvat: {path}")


def plot_roc_curves(models, X_test, y_test):
    """
    Salveaza un grafic cu curbele ROC suprapuse pentru toate modelele.

    Curba ROC reprezinta rata True Positive vs rata False Positive la
    diferite praguri de clasificare. AUC (Area Under Curve) aproape de
    1.0 indica un model excelent, AUC = 0.5 inseamna clasificare aleatoare.
    """
    print("[8] Generare grafic ROC curves...")
    fig, ax = plt.subplots(figsize=(7, 5))

    colors = ["royalblue", "darkorange", "forestgreen"]
    for (name, model), color in zip(models.items(), colors):
        y_proba     = model.predict_proba(X_test)[:, 1]
        fpr, tpr, _ = roc_curve(y_test, y_proba)
        auc         = roc_auc_score(y_test, y_proba)
        ax.plot(fpr, tpr, color=color, lw=2, label=f"{name} (AUC = {auc:.4f})")

    ax.plot([0, 1], [0, 1], "k--", lw=1, label="Random (AUC = 0.5)")
    ax.set_xlabel("False Positive Rate (1 - Specificitate)", fontsize=11)
    ax.set_ylabel("True Positive Rate (Sensibilitate / Recall)", fontsize=11)
    ax.set_title("Curbe ROC — Comparatie modele", fontsize=13)
    ax.legend(loc="lower right", fontsize=10)
    ax.grid(alpha=0.3)
    plt.tight_layout()

    path = os.path.join(RESULTS_DIR, "roc_curves.png")
    plt.savefig(path, dpi=150, bbox_inches="tight")
    plt.close()
    print(f"    Salvat: {path}")


def plot_feature_importance(rf_model, feature_names):
    """
    Salveaza graficul de feature importance al modelului Random Forest.

    Feature importance in RF masoara cat de mult contribuie fiecare
    variabila la reducerea impuritatii (Gini) in arbori. Valorile mai
    mari indica predictori mai importanti pentru clasificare.
    """
    print("[9] Generare grafic Feature Importance (Random Forest)...")
    importances = rf_model.feature_importances_
    indices     = np.argsort(importances)[::-1]   # sortare descrescatoare

    fig, ax = plt.subplots(figsize=(8, 5))
    ax.bar(
        range(len(feature_names)),
        importances[indices],
        color     = "steelblue",
        edgecolor = "white"
    )
    ax.set_xticks(range(len(feature_names)))
    ax.set_xticklabels([feature_names[i] for i in indices],
                       rotation=30, ha="right", fontsize=10)
    ax.set_ylabel("Importanta (Gini impurity reduction)", fontsize=11)
    ax.set_title("Feature Importance — Random Forest", fontsize=13)
    ax.grid(axis="y", alpha=0.3)
    plt.tight_layout()

    path = os.path.join(RESULTS_DIR, "feature_importance_RF.png")
    plt.savefig(path, dpi=150, bbox_inches="tight")
    plt.close()
    print(f"    Salvat: {path}")


# ─────────────────────────────────────────────────────────────────────────────
# 11.  SALVARE MODEL + SCALER
# ─────────────────────────────────────────────────────────────────────────────

def save_best_model(models, df_metrics, scaler):
    """
    Salveaza cel mai bun model (dupa AUC-ROC) si scaler-ul ca fisiere .pkl.

    Fisierele .pkl sunt incarcate ulterior de app.py (Streamlit) pentru
    a face predictii pe date noi introduse de utilizator.
    """
    best_name  = df_metrics.loc[df_metrics["ROC-AUC"].idxmax(), "Model"]
    best_model = models[best_name]

    model_path  = os.path.join(MODELS_DIR, "best_model.pkl")
    scaler_path = os.path.join(MODELS_DIR, "scaler.pkl")

    joblib.dump(best_model, model_path)
    joblib.dump(scaler,     scaler_path)

    best_auc = df_metrics.loc[df_metrics["ROC-AUC"].idxmax(), "ROC-AUC"]
    print(f"\n[10] Model ales: {best_name} (AUC={best_auc:.4f})")
    print(f"     Salvat: {model_path}")
    print(f"     Scaler salvat: {scaler_path}")


# ─────────────────────────────────────────────────────────────────────────────
# 12.  MAIN
# ─────────────────────────────────────────────────────────────────────────────

def main():
    print("=" * 65)
    print("  Pipeline ML — Predictia Riscului de Diabet (PIDD)")
    print("=" * 65)

    # 1. Incarcare
    df = load_data(DATASET_PATH)

    # 2. Marcare zerouri biologice ca NaN (fara imputare inca!)
    df = mark_biological_zeros(df)

    # 3. Split stratified 80/20
    X_train, X_test, y_train, y_test = split_data(df)

    # 4. Imputare cu mediana din train (DUPA split — fara Data Leakage)
    X_train, X_test = impute_with_train_median(X_train, X_test)

    # 5. Scalare (fit pe train) + SMOTE (doar pe train)
    X_train_res, y_train_res, X_test_sc, scaler = scale_and_smote(
        X_train, X_test, y_train
    )

    # 6. Modele
    models = get_models()

    # 7. Antrenare + evaluare pe test set
    df_metrics = train_and_evaluate(models, X_train_res, y_train_res, X_test_sc, y_test)

    # 8. Validare incrucisata
    df_cv = cross_validate_models(models, X_train_res, y_train_res)

    # 9. Grafice
    plot_confusion_matrices(models, X_test_sc, y_test)
    plot_roc_curves        (models, X_test_sc, y_test)
    plot_feature_importance(models["Random Forest"], FEATURE_NAMES)

    # 10. Salvare model + scaler
    save_best_model(models, df_metrics, scaler)

    # 11. Sumar final
    print("\n" + "=" * 65)
    print("  REZULTATE FINALE (Test Set)")
    print("=" * 65)
    print(df_metrics.to_string(index=False))
    print("\n  Validare Incrucisata (5-fold CV — Media +/- Std)")
    print("=" * 65)
    for _, row in df_cv.iterrows():
        print(f"  {row['Model']:22s} | "
              f"Acc={row['accuracy_mean']:.4f}+/-{row['accuracy_std']:.4f} | "
              f"F1={row['f1_mean']:.4f}+/-{row['f1_std']:.4f} | "
              f"AUC={row['roc_auc_mean']:.4f}+/-{row['roc_auc_std']:.4f}")
    print("=" * 65)
    print("\n  Pipeline finalizat. Fisierele sunt in results/ si models/")


if __name__ == "__main__":
    main()