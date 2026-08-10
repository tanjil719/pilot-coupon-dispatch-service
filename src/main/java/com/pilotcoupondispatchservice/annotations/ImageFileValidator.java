//package com.pilotcoupondispatchservice.annotations;
//
//import org.springframework.web.multipart.MultipartFile;
//
//import jakarta.validation.ConstraintValidator;
//import jakarta.validation.ConstraintValidatorContext;
//
//public class ImageFileValidator implements ConstraintValidator<ValidImage, MultipartFile> {
//
//    boolean required = true;
//
//    @Override
//    public void initialize(ValidImage constraintAnnotation) {
//        required = constraintAnnotation.required();
//    }
//
//    @Override
//    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
//
//        if (!required && file == null) {
//            return true;
//        }
//
//        if (required && file == null) {
//            context.disableDefaultConstraintViolation();
//            context.buildConstraintViolationWithTemplate("Image file is missing.").addConstraintViolation();
//            return false;
//        }
//
//        String contentType = file.getContentType();
//
//        return contentType != null && isSupportedContentType(contentType);
//    }
//
//    private boolean isSupportedContentType(String contentType) {
//        return contentType.startsWith("image");
//    }
//}
