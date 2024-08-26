package pda.keywordream.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

public class ApiUtils<T> {
    public static <T> ApiResult<T> success(T data){
        return new ApiResult<>(true, data, null);
    }

    public static <M> ApiResult<M> error(M errorMessage, HttpStatus status){
        return new ApiResult<>(false, null, new ApiError(errorMessage, status));
    }

    @Getter
    @AllArgsConstructor
    public static class ApiResult<T>{
        boolean success;
        T response;
        ApiError error;
    }

    @Getter
    private static class ApiError<M>{
        M errorMessage;
        HttpStatus httpStatus;

        ApiError(M errorMessage, HttpStatus httpStatus){
            this.errorMessage = errorMessage;
            this.httpStatus = httpStatus;
        }
    }

}
