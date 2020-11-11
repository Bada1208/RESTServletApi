package com.sysoiev.rest.controller;

import com.google.gson.Gson;
import com.sysoiev.rest.model.Specialty;
import com.sysoiev.rest.repository.SpecialtyRepository;
import com.sysoiev.rest.repository.impl.HibernateSpecialtyRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "SpecialtyServlet", urlPatterns = "/api/v1/specialties")
public class SpecialtyServlet extends HttpServlet {
    private final SpecialtyRepository specialtyRepository = new HibernateSpecialtyRepository();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        String id = req.getParameter("id");
        if (id == null) {
            List<Specialty> specialtyList = specialtyRepository.getAll();
            if (specialtyList == null) {
                resp.sendError(404);
            } else {
                writer.println(gson.toJson(specialtyList));
            }
        } else {
            Long specialtyId = Long.parseLong(id);
            Specialty specialty = specialtyRepository.getById(specialtyId);
            writer.println(gson.toJson(specialty));
        }
        writer.flush();
        writer.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Specialty specialty = gson.fromJson(req.getReader(), Specialty.class);
        specialtyRepository.create(specialty);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Specialty specialty = gson.fromJson(req.getReader(), Specialty.class);
        specialtyRepository.update(specialty);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("id") == null) {
            resp.sendError(400, "Invalid parameter id");
        } else {
            specialtyRepository.deleteById(Long.parseLong(req.getParameter("id")));
        }
    }
}