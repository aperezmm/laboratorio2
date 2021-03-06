/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.udea.servlet;

import com.udea.ejb.VehiclesFacadeLocal;
import com.udea.entity.Vehicles;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 *
 * @author alejo
 */
public class VehicleServlet extends HttpServlet {

    @EJB
    private VehiclesFacadeLocal vehiclesFacade;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try{
            String action = request.getParameter("action");
            String url = "index.jsp";
            
            if("list".equals(action)){
                List<Vehicles> findAll = vehiclesFacade.findAll();
                request.getSession().setAttribute("vehicles", findAll);
                url =  "listVehicles.jsp";
            } else if("insert".equals(action)){
                Vehicles v = new Vehicles();
                v.setLicensePlate(request.getParameter("licensePlate"));
                v.setCarBrand(request.getParameter("carBrand"));
                v.setCarModel(request.getParameter("carModel"));
                v.setPrice(request.getParameter("price"));
             /* Part _photo = request.getPart("photo");
                try(InputStream is = _photo.getInputStream()){
                    byte[] photo = new byte[is.available()];
                    is.read(photo);
                    is.close();
                    v.setPhoto(photo);
                } */ 
                vehiclesFacade.create(v);
                url = "menu.jsp";
            } else if("delete".equals(action)){
                String licensePlate = request.getParameter("licensePlate");
                Vehicles v = vehiclesFacade.find(licensePlate);
                vehiclesFacade.remove(v);
                url ="VehicleServlet?action=list";
            } else if("search".equals(action)){
                String licensePlate = request.getParameter("licensePlate");
                Vehicles v = vehiclesFacade.find(licensePlate);
                url ="findVehicle.jsp";
            } else if("edit".equals(action)){
                Vehicles v = new Vehicles();
                v.setCarModel(request.getParameter("carModel"));
                v.setPrice(request.getParameter("price"));
                vehiclesFacade.edit(v);
                url = "menu.jsp";
            }
            response.sendRedirect(url);
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
