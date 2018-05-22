package cat.xtec.ioc.dawm07eac2restaurantEnunciat;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Miriam
 */
@WebServlet(urlPatterns = {"/user"})
public class UserServlet extends HttpServlet {

    @Resource
    Validator validator;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, NamingException, JSONException {

        String action = request.getParameter("action");

        if (action.equalsIgnoreCase("formUser")) {

            formUser(request, response);

        }

        if (action.equalsIgnoreCase("newUser")) {

            newUser(request, response);

        }

    }

    private void formUser(HttpServletRequest request, HttpServletResponse response) {

        try {

            UserLocal bean = (UserLocal) request.getSession().getAttribute("userbean");

            if (bean == null) {

                bean = (UserLocal) new InitialContext().lookup("java:global/DAWM07_EAC2_Aparicio_G/User");
                request.getSession().setAttribute("userbean", bean);

            }
            
            PrintWriter out = response.getWriter();
            response.setCharacterEncoding("utf-8");
            
            JSONObject json = new JSONObject();
            
            if (bean.getUser() == null) {
                
                json.put("user", "");
                json.put("name", "");
                json.put("lastname", "");
                
            } else {
                
                json.put("user", bean.getUser());
                json.put("name", bean.getName());
                json.put("lastname", bean.getLastname());
            }

            out.print(json.toString());
            out.close();
            
            
        } catch (Exception  e) {

        }

    }

    private void newUser(HttpServletRequest request, HttpServletResponse response) {
        
        try {
            
            UserLocal bean = (UserLocal) request.getSession().getAttribute("userbean");
            bean.setUser(request.getParameter("user"));
            bean.setName(request.getParameter("name"));
            bean.setLastname(request.getParameter("lastname"));
            
            String isValid = "OK";
            
            for (ConstraintViolation c : validator.validate(bean)) {
                
                isValid = c.getMessage();
            }
            
            PrintWriter out = response.getWriter();
            response.setCharacterEncoding("utf-8");
            
            JSONObject json = new JSONObject();
            json.put("resposta", isValid);
            out.print(json.toString());
            out.close();
            
        } catch (Exception e) {
            
        }

    }

    //
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
        try {
            processRequest(request, response);
        } catch (Exception e) {
            Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, e);
        }
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
        try {
            processRequest(request, response);
        } catch (Exception e) {
            Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, e);
        }
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
