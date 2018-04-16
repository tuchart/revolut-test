package com.company.revolut.builder;

import com.company.revolut.dto.Account;
import com.company.revolut.dto.Response;
import com.company.revolut.exception.BusinessException;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;

public class ResponseBuilderTest {

    private final ResponseBuilder responseBuilder = new ResponseBuilder();

    @Test
    public void buildSuccessResponseTest() {
        // given
        String requestId = "requestId";
        Account account = new Account();
        int code = 200;
        // when
        Response response = responseBuilder.buildSuccessResponse(requestId, account, code);
        // then
        thenSuccessResponse(response);
    }

    @Test
    public void buildErrorResponseTest() {
        // given
        String requestId = "requestId";
        int code = 500;
        Exception exception = new BusinessException("error");
        // when
        Response response = responseBuilder.buildErrorResponse(requestId, code, exception.getMessage());
        // then
        thenErrorResponse(response);
    }

    private void thenSuccessResponse(Response response) {
        assertThat(response, notNullValue());
        assertThat(response.getRequestId(), is("requestId"));
        assertThat(response.getBody(), notNullValue());
        assertThat(response.getStatusCode(), is(200));
        assertThat(response.getErrorMessage(), nullValue());
    }

    private void thenErrorResponse(Response response) {
        assertThat(response, notNullValue());
        assertThat(response.getRequestId(), is("requestId"));
        assertThat(response.getBody(), nullValue());
        assertThat(response.getStatusCode(), is(500));
        assertThat(response.getErrorMessage(), is("error"));
    }
}
