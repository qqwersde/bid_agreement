package com.thoughtworks.midquiz.midquiz.client.decoder;

import com.thoughtworks.midquiz.midquiz.exception.BusinessException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class PaymentErrorDecoder implements ErrorDecoder {


    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() != 200) {
            if (response.status() == 400){
                return new BusinessException("支付失败，请检查账户", HttpStatus.BAD_REQUEST);
            }
            return new BusinessException("好像出错了，请稍后再试", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return decode(methodKey, response);
    }


}
