import joblib
import numpy as np
import streamlit as st


st.set_page_config(
    page_title = "Predictie Diabet",
    layout     = "centered",
)


@st.cache_resource
def load_model():
    """
    @st.cache_resource asigura ca fisierele sunt incarcate o singura data
    """
    model  = joblib.load("models/best_model.pkl")
    scaler = joblib.load("models/scaler.pkl")
    return model, scaler

try:
    model, scaler = load_model()
    model_loaded  = True
except FileNotFoundError:
    model_loaded  = False

# ─────────────────────────────────────────────────────────────────────────────
# TITLU SI DESCRIERE
# ─────────────────────────────────────────────────────────────────────────────

st.title("🩺 Sistem de Predictie a Riscului de Diabet")
st.markdown(
    "Introduceti valorile analizelor medicale si apasati **Analizeaza** "
    "pentru a obtine o estimare a riscului de diabet."
)
st.markdown("---")

if not model_loaded:
    st.error(
        "⚠️ Modelul nu a fost gasit. "
        "Rulati mai intai **train.py** pentru a genera fisierele din `models/`."
    )
    st.stop()

# ─────────────────────────────────────────────────────────────────────────────
# FORMULAR DE INTRODUCERE DATE
# ─────────────────────────────────────────────────────────────────────────────

st.subheader("Date medicale")

# Impartim in 2 coloane pentru un layout mai aerisit
col1, col2 = st.columns(2)

with col1:
    glucose = st.number_input(
        label    = "Glucoza plasmatica (mg/dL)",
        min_value= 50,
        max_value= 300,
        value    = 110,
        step     = 1,
        help     = "Nivelul glucozei din sange. Valori normale: 70–99 mg/dL."
    )

    blood_pressure = st.number_input(
        label    = "Tensiune arteriala diastolica (mm Hg)",
        min_value= 30,
        max_value= 140,
        value    = 72,
        step     = 1,
        help     = "Tensiunea arteriala diastolica (valoarea mica). Normal: sub 80 mm Hg."
    )

    skin_thickness = st.number_input(
        label    = "Grosime pliu cutanat tricipital (mm)",
        min_value= 5,
        max_value= 100,
        value    = 29,
        step     = 1,
        help     = "Masurarea stratului de grasime subcutanata. Normal: 10–40 mm."
    )

    insulin = st.number_input(
        label    = "Insulina serica la 2 ore (mu U/ml)",
        min_value= 10,
        max_value= 900,
        value    = 125,
        step     = 1,
        help     = "Nivelul insulinei dupa 2 ore. Normal: 16–166 mu U/ml."
    )

with col2:
    bmi = st.number_input(
        label    = "Indice de masa corporala — BMI (kg/m²)",
        min_value= 10.0,
        max_value= 70.0,
        value    = 32.0,
        step     = 0.1,
        format   = "%.1f",
        help     = "BMI = greutate(kg) / inaltime²(m). Normal: 18.5–24.9."
    )

    dpf = st.number_input(
        label    = "Diabetes Pedigree Function",
        min_value= 0.05,
        max_value= 2.50,
        value    = 0.47,
        step     = 0.01,
        format   = "%.2f",
        help     = "Probabilitate de diabet bazata pe istoricul familial. Interval: 0.08–2.42."
    )

    age = st.number_input(
        label    = "Varsta (ani)",
        min_value= 21,
        max_value= 100,
        value    = 33,
        step     = 1,
        help     = "Varsta pacientului in ani."
    )

st.markdown("---")

# ─────────────────────────────────────────────────────────────────────────────
# PREDICTIE
# ─────────────────────────────────────────────────────────────────────────────

if st.button("🔍 Analizeaza", use_container_width=True, type="primary"):

    # Construim vectorul de input in ordinea features din train.py:
    # [Glucose, BloodPressure, SkinThickness, Insulin, BMI, DPF, Age]
    input_data = np.array([[glucose, blood_pressure, skin_thickness,
                            insulin, bmi, dpf, age]])

    # Aplicam acelasi scaler folosit la antrenare
    input_scaled = scaler.transform(input_data)

    # Obtinem predictia si probabilitatile
    prediction   = model.predict(input_scaled)[0]           # 0 sau 1
    proba        = model.predict_proba(input_scaled)[0]     # [prob_0, prob_1]
    risk_pct     = round(proba[1] * 100, 1)                 # % risc diabet
    safe_pct     = round(proba[0] * 100, 1)                 # % fara risc

    st.markdown("### Rezultat")

    if prediction == 0:
        # ── Rezultat negativ (sanatos) ────────────────────────────────────
        st.success(
            f"✅ **Fara risc semnificativ de diabet**\n\n"
            f"Probabilitate estimata de diabet: **{risk_pct}%**"
        )
        st.progress(int(risk_pct), text=f"Risc: {risk_pct}%")
        st.info(
            "ℹ️ Modelul Random Forest estimeaza ca valorile introduse "
            "nu indica un risc semnificativ de diabet. "
            "Aceasta predictie are scop informativ — consultati un medic "
            "pentru un diagnostic oficial."
        )
    else:
        # ── Rezultat pozitiv (risc diabet) ───────────────────────────────
        st.error(
            f"⚠️ **Risc ridicat de diabet**\n\n"
            f"Probabilitate estimata de diabet: **{risk_pct}%**"
        )
        st.progress(int(risk_pct), text=f"Risc: {risk_pct}%")
        st.warning(
            "⚕️ Modelul Random Forest estimeaza un risc ridicat de diabet "
            "pe baza valorilor introduse. "
            "Va recomandam sa consultati un medic specialist pentru "
            "investigatii suplimentare si un diagnostic oficial."
        )

    # ── Detalii suplimentare ──────────────────────────────────────────────
    with st.expander("📊 Detalii predictie"):
        st.write(f"**Model folosit:** Random Forest (100 arbori de decizie)")
        st.write(f"**Probabilitate clasa 0 (Sanatos):** {safe_pct}%")
        st.write(f"**Probabilitate clasa 1 (Diabetic):** {risk_pct}%")
        st.write("**Date introduse:**")
        st.json({
            "Glucoza (mg/dL)"            : glucose,
            "Tensiune arteriala (mm Hg)" : blood_pressure,
            "Grosime pliu cutanat (mm)"  : skin_thickness,
            "Insulina (mu U/ml)"         : insulin,
            "BMI (kg/m²)"               : bmi,
            "Diabetes Pedigree Function" : dpf,
            "Varsta (ani)"               : age,
        })

# ─────────────────────────────────────────────────────────────────────────────
# FOOTER
# ─────────────────────────────────────────────────────────────────────────────

st.markdown("---")
st.caption(
    "Proiect realizat pe setul de date **Pima Indians Diabetes Database** (PIDD). "
    "Model: Random Forest antrenat cu scikit-learn. "
    "Aceasta aplicatie are scop exclusiv educational."
)