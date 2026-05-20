import os
import warnings

import joblib
import matplotlib
import numpy as np
import pandas as pd

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


DATASET_PATH = "dataset/PIDD.csv"
MODELS_DIR = "models"
RESULTS_DIR = "results"
RANDOM_STATE = 42           # seed pentru reproductibilitate
TEST_SIZE = 0.20            # 80% train / 20% test
CV_FOLDS = 5                # numarul de pliuri pentru validarea incrucisata

BIOLOGICAL_ZERO_COLS = ["Glucose", "BloodPressure", "SkinThickness", "Insulin", "BMI"]

FEATURE_NAMES = ["Glucose", "BloodPressure", "SkinThickness",
                 "Insulin", "BMI", "DiabetesPedigreeFunction", "Age"]

os.makedirs(MODELS_DIR, exist_ok=True)
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
        test_size = TEST_SIZE,
        random_state = RANDOM_STATE,
        stratify = y          # pastreaza distributia claselor
    )

    print(f"[2] Split: train={X_train.shape[0]} | test={X_test.shape[0]}")
    print(f"    Distributie train — 0:{(y_train==0).sum()} | 1:{(y_train==1).sum()}")
    print(f"    Distributie test  — 0:{(y_test==0).sum()}  | 1:{(y_test==1).sum()}")
    return X_train, X_test, y_train, y_test


def mark_biological_zeros(df: pd.DataFrame) -> pd.DataFrame:
    """
    Inlocuieste zerourile biologice imposibile cu NaN.
    """
    df = df.copy()
    for col in BIOLOGICAL_ZERO_COLS:
        df[col] = df[col].replace(0, np.nan)
    return df


def impute_with_train_median(X_train: np.ndarray, X_test: np.ndarray) -> tuple:
    X_train = X_train.copy().astype(float)
    X_test = X_test.copy().astype(float)

    print("[3] Imputare cu mediana:")
    for i, col_name in enumerate(FEATURE_NAMES):
        if col_name not in BIOLOGICAL_ZERO_COLS:
            continue

        train = np.isnan(X_train[:, i]).sum()
        test = np.isnan(X_test[:, i]).sum()

        if train == 0 and test == 0:
            continue

        median_val = np.nanmedian(X_train[:, i])

        X_train[np.isnan(X_train[:, i]), i] = median_val
        X_test[np.isnan(X_test[:, i]),  i] = median_val

        print(f"    [{col_name}] median_train={median_val:.2f} | "
              f"imputate: train={train}, test={test}")

    return X_train, X_test


def scale_and_smote(X_train, X_test, y_train):
    scaler = StandardScaler()
    X_train_scalled = scaler.fit_transform(X_train)
    X_test_scaled = scaler.transform(X_test)

    smote = SMOTE(random_state=RANDOM_STATE)
    X_train_res, y_train_res = smote.fit_resample(X_train_scalled, y_train)

    return X_train_res, y_train_res, X_test_scaled, scaler



def get_models() -> dict:
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
            n_jobs       = -1
        ),
    }


def train_and_evaluate(models, X_train, y_train, X_test, y_test) -> pd.DataFrame:
    print("\n[4] Antrenare si evaluare modele:")
    results = []

    for name, model in models.items():
        model.fit(X_train, y_train)

        y_pred  = model.predict(X_test)
        y_proba = model.predict_proba(X_test)[:, 1] # probabilitati, extragem doar 1 pentru ROC-AUC

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
    return df_metrics


def cross_validate_models(models, X_train, y_train) -> pd.DataFrame:
    print("\n[5] Validare incrucisata (5-fold CV pe train set):")
    skf = StratifiedKFold(n_splits=CV_FOLDS, shuffle=True, random_state=RANDOM_STATE)
    scoring = ["accuracy", "precision", "recall", "f1", "roc_auc"]
    cv_rows = []

    for name, model in models.items():
        scores = cross_validate(model, X_train, y_train, cv=skf, scoring=scoring)
        row = {"Model": name}
        for metric in scoring:
            mean_val = scores[f"test_{metric}"].mean()
            std_val = scores[f"test_{metric}"].std()
            row[f"{metric}_mean"] = round(mean_val, 4)
            row[f"{metric}_std"] = round(std_val,  4)
        cv_rows.append(row)
        print(f"    {name:22s} | CV Acc={row['accuracy_mean']:.4f}+/-{row['accuracy_std']:.4f} "
              f"| CV F1={row['f1_mean']:.4f}+/-{row['f1_std']:.4f}")

    df_cv = pd.DataFrame(cv_rows)
    df_cv.to_csv(os.path.join(RESULTS_DIR, "cv_metrics.csv"), index=False)
    return df_cv


def plot_confusion_matrices(models, X_test, y_test):
    for name, model in models.items():
        y_pred = model.predict(X_test)
        cm = confusion_matrix(y_test, y_pred)

        fig, ax = plt.subplots(figsize=(5, 4))
        disp = ConfusionMatrixDisplay(
            confusion_matrix = cm,
            display_labels   = ["Sanatos (0)", "Diabetic (1)"]
        )
        disp.plot(ax=ax, colorbar=False, cmap="Blues")
        ax.set_title(f"Confusion Matrix — {name}", fontsize=12, pad=12)
        plt.tight_layout()

        fname = name.replace(" ", "_")
        path = os.path.join(RESULTS_DIR, f"confusion_matrix_{fname}.png")
        plt.savefig(path, dpi=150, bbox_inches="tight")
        plt.close()


def plot_roc_curves(models, X_test, y_test):
    fig, ax = plt.subplots(figsize=(7, 5))

    colors = ["royalblue", "darkorange", "forestgreen"]
    for (name, model), color in zip(models.items(), colors):
        y_proba = model.predict_proba(X_test)[:, 1]
        fpr, tpr, _ = roc_curve(y_test, y_proba)
        auc = roc_auc_score(y_test, y_proba)
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


def plot_feature_importance(rf_model, feature_names):
    importances = rf_model.feature_importances_
    indices = np.argsort(importances)[::-1]   # sortare descrescatoare

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


def save_best_model(models, df_metrics, scaler):
    best_name = df_metrics.loc[df_metrics["ROC-AUC"].idxmax(), "Model"]
    best_model = models[best_name]

    model_path  = os.path.join(MODELS_DIR, "best_model.pkl")
    scaler_path = os.path.join(MODELS_DIR, "scaler.pkl")

    joblib.dump(best_model, model_path) # save to file
    joblib.dump(scaler, scaler_path)

    best_auc = df_metrics.loc[df_metrics["ROC-AUC"].idxmax(), "ROC-AUC"]
    print(f"\n Model ales: {best_name} (AUC={best_auc:.4f})")
    print(f"     Salvat: {model_path}")
    print(f"     Scaler salvat: {scaler_path}")



def main():
    df = load_data(DATASET_PATH)

    df = mark_biological_zeros(df)

    X_train, X_test, y_train, y_test = split_data(df)

    X_train, X_test = impute_with_train_median(X_train, X_test)

    X_train_res, y_train_res, X_test_sc, scaler = scale_and_smote(
        X_train, X_test, y_train
    )

    models = get_models()

    df_metrics = train_and_evaluate(models, X_train_res, y_train_res, X_test_sc, y_test)

    df_cv = cross_validate_models(models, X_train_res, y_train_res)

    plot_confusion_matrices(models, X_test_sc, y_test)
    plot_roc_curves(models, X_test_sc, y_test)
    plot_feature_importance(models["Random Forest"], FEATURE_NAMES)

    save_best_model(models, df_metrics, scaler)

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


if __name__ == "__main__":
    main()