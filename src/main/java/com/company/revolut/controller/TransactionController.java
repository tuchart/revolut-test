package com.company.revolut.controller;

import com.company.revolut.builder.ResponseBuilder;
import com.company.revolut.dto.Response;
import com.company.revolut.dto.Transaction;
import com.company.revolut.service.dao.TransactionDaoAdapter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigInteger;
import java.util.List;

import static org.eclipse.jetty.http.HttpStatus.*;

@Path("/transaction")
@Slf4j
public class TransactionController {

    @Inject
    private TransactionDaoAdapter transactionDaoAdapter;
    @Inject
    private ResponseBuilder responseBuilder;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    public Response getAllTransactions(@QueryParam("requestId") String requestId) {
        MDC.put("requestId", requestId);
        try {
            List<Transaction> transactions = transactionDaoAdapter.findAll();
            return responseBuilder.buildSuccessResponse(requestId, transactions, OK_200);
        } catch (Exception e) {
            log.error("getAllTransactions.error", e);
            return responseBuilder.buildErrorResponse(requestId, INTERNAL_SERVER_ERROR_500, e.toString());
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response getTransaction(@QueryParam("requestId") String requestId, @PathParam("id") BigInteger id) {
        MDC.put("requestId", requestId);
        MDC.put("id", id.toString());
        try {
            Transaction transaction = transactionDaoAdapter.findOne(id);
            return responseBuilder.buildSuccessResponse(requestId, transaction, OK_200);
        } catch (Exception e) {
            log.error("getTransaction.error", e);
            return responseBuilder.buildErrorResponse(requestId, INTERNAL_SERVER_ERROR_500, e.toString());
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    public Response makeTransaction(@QueryParam("requestId") String requestId, Transaction transaction) {
        MDC.put("requestId", requestId);
        try {
            transactionDaoAdapter.makeTransaction(transaction);
            return responseBuilder.buildSuccessResponse(requestId, transaction, CREATED_201);
        } catch (Exception e) {
            log.error("makeTransaction.error", e);
            return responseBuilder.buildErrorResponse(requestId, INTERNAL_SERVER_ERROR_500, e.toString());
        }
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response rollbackTransaction(@QueryParam("requestId") String requestId, @PathParam("id") BigInteger transactionId) {
        MDC.put("requestId", requestId);
        MDC.put("transactionId", transactionId.toString());
        try {
            transactionDaoAdapter.rollbackTransaction(transactionId);
            return responseBuilder.buildSuccessResponse(requestId, null, OK_200);
        } catch (Exception e) {
            log.error("rollbackTransaction.error", e);
            return responseBuilder.buildErrorResponse(requestId, INTERNAL_SERVER_ERROR_500, e.toString());
        }
    }
}
