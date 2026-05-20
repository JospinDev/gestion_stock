package com.jospin.gestion_stock.validations;

import lombok.Getter;

@Getter
public enum ValidationErrorType {

    FIELD_REQUIRED("FIELD_REQUIRED", "Le champ est obligatoire"),
    FIELD_INVALID("FIELD_INVALID", "La valeur du champ est invalide"),
    FIELD_TOO_SHORT("FIELD_TOO_SHORT", "La valeur est trop courte"),
    FIELD_TOO_LONG("FIELD_TOO_LONG", "La valeur est trop longue"),

    CODE_INVALID("CODE_INVALID", "Le format du code est invalide"),
    CODE_ALREADY_EXISTS("CODE_ALREADY_EXISTS", "Ce code existe déjà"),

    ENTREPOT_NOT_FOUND("ENTREPOT_NOT_FOUND", "Entrepôt non trouvé"),
    ENTREPOT_HAS_ARTICLES("ENTREPOT_HAS_ARTICLES", "Impossible de supprimer un entrepôt contenant des articles"),

    ARTICLE_NOT_FOUND("ARTICLE_NOT_FOUND", "Article non trouvé"),
    ARTICLE_CODE_EXISTS_IN_ENTREPOT("ARTICLE_CODE_EXISTS_IN_ENTREPOT", "Cet article existe déjà dans cet entrepôt"),

    INSUFFICIENT_STOCK("INSUFFICIENT_STOCK", "Stock insuffisant"),
    INVALID_QUANTITY("INVALID_QUANTITY", "Quantité invalide"),
    INVALID_PRICE("INVALID_PRICE", "Prix invalide"),

    MOUVEMENT_NOT_FOUND("MOUVEMENT_NOT_FOUND", "Mouvement non trouvé"),
    INVALID_MOUVEMENT_TYPE("INVALID_MOUVEMENT_TYPE", "Type de mouvement invalide"),

    VALIDATION_FAILED("VALIDATION_FAILED", "Échec de validation"),
    DUPLICATE_ENTRY("DUPLICATE_ENTRY", "Entrée en double détectée");

    private final String code;
    private final String message;

    ValidationErrorType(String code, String message) {
        this.code = code;
        this.message = message;
    }
}