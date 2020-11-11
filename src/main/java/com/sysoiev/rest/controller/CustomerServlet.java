package com.sysoiev.rest.controller;

import com.google.gson.Gson;
import com.sysoiev.rest.model.Customer;
import com.sysoiev.rest.repository.CustomerRepository;
import com.sysoiev.rest.repository.impl.HibernateCustomerRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


@WebServlet(name = "CustomerServlet", urlPatterns = "/api/v1/customers")
public class CustomerServlet extends HttpServlet {
    private final CustomerRepository customerRepository = new HibernateCustomerRepository();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        String id = req.getParameter("id");
        if (id == null) {
            List<Customer> customerList = customerRepository.getAll();
            if (customerList == null) {
                resp.sendError(404);
            } else {
                writer.println(gson.toJson(customerList));
            }
        } else {
            Long customerId = Long.parseLong(id);
            Customer customer = customerRepository.getById(customerId);
            writer.println(gson.toJson(customer));
        }
        writer.flush();
        writer.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Customer customer = gson.fromJson(req.getReader(), Customer.class);
        customerRepository.create(customer);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Customer customer = gson.fromJson(req.getReader(), Customer.class);
        customerRepository.update(customer);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("id") == null) {
            resp.sendError(400, "Invalid parameter id");
        } else {
            customerRepository.deleteById(Long.parseLong(req.getParameter("id")));
        }
    }
}