const COUNTRIES_API_URL = "https://restcountries.com/v3.1/all?fields=name,flags";
const REGISTER_API_URL = "https://masksoft.com.mx/register";

const studentForm = document.getElementById("studentForm");
const clearButton = document.getElementById("clearButton");
const countrySelect = document.getElementById("country");
const serverResponse = document.getElementById("serverResponse");

document.addEventListener("DOMContentLoaded", () => {
    loadCountries();
    registerEventListeners();
});

/**
 * Registra los eventos principales del formulario.
 * - submit: intenta validar y enviar el formulario.
 * - click en limpiar: reinicia el estado de la interfaz.
 * - input/change: revalida campos conforme el usuario corrige datos.
 */
function registerEventListeners() {
    studentForm.addEventListener("submit", handleFormSubmit);
    clearButton.addEventListener("click", clearForm);

    document.getElementById("name").addEventListener("input", () => validateSingleField("name"));
    document.getElementById("lastName").addEventListener("input", () => validateSingleField("lastName"));
    document.getElementById("email").addEventListener("input", () => validateSingleField("email"));
    document.getElementById("account").addEventListener("input", () => validateSingleField("account"));
    document.getElementById("avg").addEventListener("input", () => validateSingleField("avg"));
    document.getElementById("country").addEventListener("change", () => validateSingleField("country"));
}

/**
 * Carga el catálogo de países desde la API externa y llena el select.
 */
async function loadCountries() {
    try {
        const response = await fetch(COUNTRIES_API_URL);

        if (!response.ok) {
            throw new Error("No fue posible obtener el catálogo de países.");
        }

        const countries = await response.json();

        countries.sort((a, b) => a.name.common.localeCompare(b.name.common, "es"));

        countrySelect.innerHTML = '<option value="">Seleccione un país</option>';

        countries.forEach((country) => {
            const option = document.createElement("option");
            option.value = country.name.common;
            option.textContent = country.name.common;
            countrySelect.appendChild(option);
        });
    } catch (error) {
        countrySelect.innerHTML = '<option value="">No se pudieron cargar los países</option>';
        showServerResponse("Error al cargar el catálogo de países.", "error");
        console.error("Error en loadCountries:", error);
    }
}

/**
 * Maneja el envío del formulario.
 * Solo realiza la petición si todos los campos pasan las validaciones.
 */
async function handleFormSubmit(event) {
    event.preventDefault();

    hideServerResponse();
    clearValidationErrors();

    const formValues = getFormValues();
    const isValid = validateForm(formValues);

    if (!isValid) {
        showServerResponse(
            "El formulario contiene errores. Corrija los campos marcados antes de enviar.",
            "error"
        );
        return;
    }

    const formData = buildFormData(formValues);

    try {
        const response = await fetch(REGISTER_API_URL, {
            method: "POST",
            body: formData
        });

        const data = await response.json();

        if (response.ok) {
            showServerResponse(
                data.message || "Los datos se guardaron correctamente.",
                "success"
            );
            studentForm.reset();
            clearValidationStyles();
        } else {
            showServerResponse(
                data.message || "Ocurrió un error al guardar la información.",
                "error"
            );
        }
    } catch (error) {
        showServerResponse(
            "No fue posible conectar con el servicio de registro.",
            "error"
        );
        console.error("Error en handleFormSubmit:", error);
    }
}

/**
 * Obtiene y normaliza los valores del formulario.
 */
function getFormValues() {
    return {
        name: document.getElementById("name").value.trim(),
        lastName: document.getElementById("lastName").value.trim(),
        email: document.getElementById("email").value.trim(),
        account: document.getElementById("account").value.trim(),
        avg: document.getElementById("avg").value.trim(),
        country: document.getElementById("country").value.trim()
    };
}

/**
 * Valida todo el formulario.
 * Devuelve true solo si todos los campos cumplen las reglas.
 */
function validateForm(values) {
    let isValid = true;

    if (!validateName(values.name)) {
        setFieldError("name", getNameError(values.name));
        isValid = false;
    } else {
        clearFieldError("name");
    }

    if (!validateLastName(values.lastName)) {
        setFieldError("lastName", getLastNameError(values.lastName));
        isValid = false;
    } else {
        clearFieldError("lastName");
    }

    if (!validateEmail(values.email)) {
        setFieldError("email", getEmailError(values.email));
        isValid = false;
    } else {
        clearFieldError("email");
    }

    if (!validateAccount(values.account)) {
        setFieldError("account", getAccountError(values.account));
        isValid = false;
    } else {
        clearFieldError("account");
    }

    if (!validateAvg(values.avg)) {
        setFieldError("avg", getAvgError(values.avg));
        isValid = false;
    } else {
        clearFieldError("avg");
    }

    if (!validateCountry(values.country)) {
        setFieldError("country", getCountryError(values.country));
        isValid = false;
    } else {
        clearFieldError("country");
    }

    return isValid;
}

/**
 * Valida un solo campo cuando el usuario lo modifica.
 * Esto mejora la experiencia al corregir errores en tiempo real.
 */
function validateSingleField(fieldName) {
    const values = getFormValues();

    switch (fieldName) {
        case "name":
            if (!validateName(values.name)) {
                setFieldError("name", getNameError(values.name));
            } else {
                clearFieldError("name");
            }
            break;

        case "lastName":
            if (!validateLastName(values.lastName)) {
                setFieldError("lastName", getLastNameError(values.lastName));
            } else {
                clearFieldError("lastName");
            }
            break;

        case "email":
            if (!validateEmail(values.email)) {
                setFieldError("email", getEmailError(values.email));
            } else {
                clearFieldError("email");
            }
            break;

        case "account":
            if (!validateAccount(values.account)) {
                setFieldError("account", getAccountError(values.account));
            } else {
                clearFieldError("account");
            }
            break;

        case "avg":
            if (!validateAvg(values.avg)) {
                setFieldError("avg", getAvgError(values.avg));
            } else {
                clearFieldError("avg");
            }
            break;

        case "country":
            if (!validateCountry(values.country)) {
                setFieldError("country", getCountryError(values.country));
            } else {
                clearFieldError("country");
            }
            break;
    }
}

/**
 * Reglas de validación para nombre.
 * - Obligatorio
 * - Al menos 2 caracteres
 * - Solo letras y espacios
 */
function validateName(value) {
    const regex = /^[A-Za-zÁÉÍÓÚáéíóúÑñÜü\s]+$/;
    return value.length >= 2 && regex.test(value);
}

function getNameError(value) {
    if (!value) {
        return "El nombre es obligatorio.";
    }
    if (value.length < 2) {
        return "El nombre debe tener al menos 2 caracteres.";
    }
    return "El nombre solo puede contener letras y espacios.";
}

/**
 * Reglas de validación para apellido.
 * - Obligatorio
 * - Al menos 2 caracteres
 * - Solo letras y espacios
 */
function validateLastName(value) {
    const regex = /^[A-Za-zÁÉÍÓÚáéíóúÑñÜü\s]+$/;
    return value.length >= 2 && regex.test(value);
}

function getLastNameError(value) {
    if (!value) {
        return "El apellido es obligatorio.";
    }
    if (value.length < 2) {
        return "El apellido debe tener al menos 2 caracteres.";
    }
    return "El apellido solo puede contener letras y espacios.";
}

/**
 * Reglas de validación para correo.
 * - Obligatorio
 * - Formato de email válido
 */
function validateEmail(value) {
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return regex.test(value);
}

function getEmailError(value) {
    if (!value) {
        return "El correo electrónico es obligatorio.";
    }
    return "Ingrese un correo electrónico válido.";
}

/**
 * Reglas de validación para número de cuenta.
 * - Obligatorio
 * - Solo números
 * - Entre 8 y 12 dígitos
 */
function validateAccount(value) {
    const regex = /^[0-9]{8,12}$/;
    return regex.test(value);
}

function getAccountError(value) {
    if (!value) {
        return "El número de cuenta es obligatorio.";
    }
    if (!/^[0-9]+$/.test(value)) {
        return "El número de cuenta solo debe contener dígitos.";
    }
    return "El número de cuenta debe tener entre 8 y 12 dígitos.";
}

/**
 * Reglas de validación para promedio.
 * - Obligatorio
 * - Número válido
 * - Entre 0 y 10
 * - Máximo 2 decimales
 */
function validateAvg(value) {
    if (!value) {
        return false;
    }

    const regex = /^(10(\.0{1,2})?|[0-9](\.[0-9]{1,2})?)$/;
    const numericValue = parseFloat(value);

    return regex.test(value) && !Number.isNaN(numericValue) && numericValue >= 0 && numericValue <= 10;
}

function getAvgError(value) {
    if (!value) {
        return "El promedio es obligatorio.";
    }
    if (Number.isNaN(parseFloat(value))) {
        return "El promedio debe ser un número válido.";
    }
    if (parseFloat(value) < 0 || parseFloat(value) > 10) {
        return "El promedio debe estar entre 0 y 10.";
    }
    return "El promedio puede tener máximo 2 decimales.";
}

/**
 * Reglas de validación para país.
 * - Obligatorio
 */
function validateCountry(value) {
    return value !== "";
}

function getCountryError(value) {
    if (!value) {
        return "Debe seleccionar un país.";
    }
    return "";
}

/**
 * Construye el FormData con los nombres exactos esperados por la API.
 */
function buildFormData(values) {
    const formData = new FormData();
    formData.append("name", values.name);
    formData.append("email", values.email);
    formData.append("account", values.account);
    formData.append("lastName", values.lastName);
    formData.append("avg", values.avg);
    formData.append("country", values.country);
    return formData;
}

/**
 * Muestra error en un campo y agrega estilo visual de error.
 */
function setFieldError(fieldName, message) {
    const errorElement = document.getElementById(`error-${fieldName}`);
    const fieldElement = document.getElementById(fieldName);

    if (errorElement) {
        errorElement.textContent = message;
    }

    if (fieldElement) {
        fieldElement.classList.add("input-error");
        fieldElement.classList.remove("input-success");
    }
}

/**
 * Limpia el error de un campo y aplica estilo visual correcto.
 */
function clearFieldError(fieldName) {
    const errorElement = document.getElementById(`error-${fieldName}`);
    const fieldElement = document.getElementById(fieldName);

    if (errorElement) {
        errorElement.textContent = "";
    }

    if (fieldElement) {
        fieldElement.classList.remove("input-error");
        if (fieldElement.value.trim() !== "") {
            fieldElement.classList.add("input-success");
        } else {
            fieldElement.classList.remove("input-success");
        }
    }
}

/**
 * Limpia todos los mensajes de error.
 */
function clearValidationErrors() {
    const errorElements = document.querySelectorAll(".error-message");
    errorElements.forEach((element) => {
        element.textContent = "";
    });
}

/**
 * Limpia clases visuales de validación.
 */
function clearValidationStyles() {
    const formFields = studentForm.querySelectorAll("input, select");
    formFields.forEach((field) => {
        field.classList.remove("input-error", "input-success");
    });
}

/**
 * Muestra un mensaje general.
 */
function showServerResponse(message, type) {
    serverResponse.textContent = message;
    serverResponse.className = `server-response ${type}`;
}

/**
 * Oculta el mensaje general.
 */
function hideServerResponse() {
    serverResponse.textContent = "";
    serverResponse.className = "server-response hidden";
}

/**
 * Limpia completamente el formulario.
 */
function clearForm() {
    studentForm.reset();
    clearValidationErrors();
    clearValidationStyles();
    hideServerResponse();
}