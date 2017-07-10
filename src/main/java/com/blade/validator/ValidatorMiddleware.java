package com.blade.validator;

import com.blade.mvc.hook.Invoker;
import com.blade.mvc.hook.WebHook;
import com.blade.validator.annotation.*;
import com.blade.validator.exception.ValidateException;
import lombok.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 参数校验中间件
 * <p>
 * Created by biezhi on 10/07/2017.
 */
public class ValidatorMiddleware implements WebHook {

    public boolean before(Invoker invoker) {
        Object[] args = invoker.getParameters();
        Method method = invoker.getAction();

        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            if (null != parameter.getAnnotation(Valid.class)) {
                try {
                    validate(args[i]);
                } catch (ValidateException e) {
                    throw e;
                } catch (Exception e) {
                    throw new ValidateException(e.getMessage());
                }
            }
        }
        return true;
    }

    private void validate(@NonNull Object object) throws IllegalAccessException {
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {

            field.setAccessible(true);
            Object fieldValue = field.get(object);

            NotNull notNull = field.getAnnotation(NotNull.class);
            if (null != notNull) {
                validateNotNull(notNull.message(), fieldValue);
            }
            NotEmpty notEmpty = field.getAnnotation(NotEmpty.class);
            if (null != notEmpty) {
                validateNotEmpty(notEmpty.message(), fieldValue);
            }
            Length length = field.getAnnotation(Length.class);
            if (null != length) {
                validateLength(length, fieldValue);
            }
            Max max = field.getAnnotation(Max.class);
            if (null != max) {
                validateMax(max, fieldValue);
            }
            Email email = field.getAnnotation(Email.class);
            if (null != email) {
                validateEmail(email, fieldValue);
            }
            Url url = field.getAnnotation(Url.class);
            if (null != url) {
                validateUrl(url, fieldValue);
            }
        }
    }

    private void validateUrl(Url url, Object fieldValue) {
        if (null != fieldValue) {
            String regex = "(https?://(w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*(\\??(.+=.*)?(&.+=.*)?)?";
            if (!Pattern.matches(regex, fieldValue.toString())) {
                throw new ValidateException(url.message());
            }
        }
    }

    public void validateNotNull(String msg, Object val) {
        if (null == val) {
            throw new ValidateException(msg);
        }
    }

    public void validateNotEmpty(String msg, Object val) {
        if (null == val) {
            throw new ValidateException(msg);
        }
        if (val instanceof String && val.toString().isEmpty()) {
            throw new ValidateException(msg);
        }
    }

    public void validateLength(Length length, Object val) {
        if (null != val) {
            if (val.toString().length() > length.value()) {
                throw new ValidateException(length.message());
            }
        }
    }

    public void validateMax(Max max, Object val) {
        if (null != val) {
            Double d = Double.valueOf(val.toString());
            if (d > max.value()) {
                throw new ValidateException(max.message());
            }
        }
    }

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public void validateEmail(Email email, Object val) {
        if (null != val) {
            Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(val.toString());
            if (!matcher.find()) {
                throw new ValidateException(email.message());
            }
        }
    }

}
