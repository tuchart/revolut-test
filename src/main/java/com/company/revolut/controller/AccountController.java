package com.company.revolut.controller;

import com.company.revolut.builder.ResponseBuilder;
import com.company.revolut.dto.Account;
import com.company.revolut.dto.Response;
import com.company.revolut.service.dao.AccountDaoAdapter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigInteger;
import java.util.List;

import static org.eclipse.jetty.http.HttpStatus.*;

@Path("/account")
@Slf4j
public class AccountController {

    @Inject
    private AccountDaoAdapter accountDaoAdapter;
    @Inject
    private ResponseBuilder responseBuilder;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    public Response getAllAccounts(@QueryParam("requestId") String requestId) {
        try {
            MDC.put("requestId", requestId);
            List<Account> accounts = accountDaoAdapter.getAllAccounts();
            return responseBuilder.buildSuccessResponse(requestId, accounts, OK_200);
        } catch (Exception e) {
            log.error("getAllAccounts.error", e);
            return responseBuilder.buildErrorResponse(requestId, INTERNAL_SERVER_ERROR_500, e.toString());
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response getAccount(@QueryParam("requestId") String requestId, @PathParam("id") BigInteger id) {
        try {
            MDC.put("requestId", requestId);
            Account account = accountDaoAdapter.findOne(id);
            return responseBuilder.buildSuccessResponse(requestId, account, OK_200);
        } catch (Exception e) {
            log.error("getAccount.error", e);
            return responseBuilder.buildErrorResponse(requestId, INTERNAL_SERVER_ERROR_500, e.toString());
        }
    }

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAccount(@QueryParam("requestId") String requestId, Account account) {
        try {
            MDC.put("requestId", requestId);
            Account result = accountDaoAdapter.createAccount(account);
            return responseBuilder.buildSuccessResponse(requestId, result, CREATED_201);
        } catch (Exception e) {
            log.error("createAccount.error", e);
            return responseBuilder.buildErrorResponse(requestId, INTERNAL_SERVER_ERROR_500, e.getMessage());
        }
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response updateAccount(@QueryParam("requestId") String requestId, Account account) {
        MDC.put("requestId", requestId);
        try {
            Account modifiedAccount = accountDaoAdapter.update(account);
            return responseBuilder.buildSuccessResponse(requestId, modifiedAccount, OK_200);
        } catch (Exception e) {
            log.error("updateAccount.error", e);
            return responseBuilder.buildErrorResponse(requestId, INTERNAL_SERVER_ERROR_500, e.toString());
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response deleteAccount(@QueryParam("requestId") String requestId, @PathParam("id") BigInteger id) {
        MDC.put("reuqestId", requestId);
        try {
            accountDaoAdapter.delete(id);
            return responseBuilder.buildSuccessResponse(requestId, null, OK_200);
        } catch (Exception e) {
            log.error("deleteAccount.error", e);
            return responseBuilder.buildErrorResponse(requestId, INTERNAL_SERVER_ERROR_500, e.toString());
        }
    }
}
