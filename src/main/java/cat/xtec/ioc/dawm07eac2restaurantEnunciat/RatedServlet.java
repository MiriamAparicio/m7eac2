package cat.xtec.ioc.dawm07eac2restaurantEnunciat;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Miriam
 */
@WebServlet(urlPatterns = {"/rated"})

public class RatedServlet extends HttpServlet {

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

        String action = request.getParameter("action");

        if (action.equalsIgnoreCase("ratedRestList")) {

            ratedRestList(request, response);

        }

        if (action.equalsIgnoreCase("deleteRatedRest")) {

            deleteRatedRest(request, response);

        }

    }

    private void ratedRestList(HttpServletRequest request, HttpServletResponse response) {

        try {

            response.setCharacterEncoding("utf-8");
            PrintWriter out = response.getWriter();

            RateLocal ratebean = (RateLocal) request.getSession().getAttribute("ratebean");

            if (ratebean == null) {

                ratebean = (RateLocal) new InitialContext().lookup("java:global/DAWM07_EAC2_Aparicio_G/Rate");

                ratebean.setRatedRest(new ArrayList<RestClass>());
                request.getSession().setAttribute("ratebean", ratebean);

            }

            JSONObject json = new JSONObject();
            JSONArray arrayRest = new JSONArray();

            for (RestClass rest : ratebean.getRatedRest()) {

                LinkedHashMap<String, String> jsonOrderedMap = new LinkedHashMap<String, String>();
                jsonOrderedMap.put("name", rest.getName());
                jsonOrderedMap.put("rate", rest.getValoracio().toString());
                jsonOrderedMap.put("med", rest.getMitjana().toString());
                jsonOrderedMap.put("rateList", rest.getTotesValoracions().toString());

                JSONObject member = new JSONObject(jsonOrderedMap);

                arrayRest.put(member);
            }

            json.put("jsonArray", arrayRest);
            out.print(json.toString());
            out.close();

        } catch (Exception e) {

        }

    }

    private void deleteRatedRest(HttpServletRequest request, HttpServletResponse response) {

        try {

            response.setCharacterEncoding("utf-8");
            PrintWriter out = response.getWriter();

            RateLocal ratebean = (RateLocal) request.getSession().getAttribute("ratebean");
            String restAEliminar = request.getParameter("rest");

            for (RestClass rest : ratebean.getRatedRest()) {

                if (rest.getName().equalsIgnoreCase(restAEliminar)) {

                    rest.eliminaUltimaValoracio();
                    ratebean.getRatedRest().remove(rest);
                    break;
                }
            }

            JSONObject json = new JSONObject();
            json.put("resposta", "OK");
            out.print(json.toString());
            out.close();

        } catch (Exception e) {

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
