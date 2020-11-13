package com.sysoiev.rest.controller;

import com.google.gson.Gson;
import com.sysoiev.rest.model.Account;
import com.sysoiev.rest.repository.AccountRepository;
import com.sysoiev.rest.repository.impl.HibernateAccountRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "AccountServlet", urlPatterns = "/api/v1/accounts")
public class AccountServlet extends HttpServlet {
    private final AccountRepository accountRepository = new HibernateAccountRepository();
    private final Gson gson = new Gson();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        String id = req.getParameter("id");
        if (id == null) {
            List<Account> accountList = accountRepository.getAll();
            if (accountList == null) {
                resp.sendError(404);
            } else {
                writer.println(gson.toJson(accountList));
            }
        } else {
            Long accountId = Long.parseLong(id);
            Account account = accountRepository.getById(accountId);
            writer.println(gson.toJson(account));
        }
        writer.flush();
        writer.close();
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Account account = gson.fromJson(req.getReader(), Account.class);
        accountRepository.create(account);
    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Account account = gson.fromJson(req.getReader(), Account.class);
        accountRepository.update(account);
    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("id") == null) {
            resp.sendError(400, "Invalid parameter id");
        } else {
            accountRepository.deleteById(Long.parseLong(req.getParameter("id")));
        }
    }
}
