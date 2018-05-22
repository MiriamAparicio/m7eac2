package cat.xtec.ioc.dawm07eac2restaurantEnunciat;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import static java.lang.System.console;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.validation.Validator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Miriam
 */
@WebServlet(name = "Restaurant", urlPatterns = {"/Restaurant"})
@MultipartConfig(location = "C:\\Users\\USUARIO\\Desktop\\temp")

public class Restaurant extends HttpServlet {

    @EJB
    private ValidateRestBeanLocal validation;

    private List<RestClass> restaurants = new ArrayList<RestClass>();

    @Override
    public void init(ServletConfig config) throws ServletException {

        super.init(config);

        //llegim els noms dels init parameter i contruim els objectes restaurant
        Enumeration e = config.getInitParameterNames();

        while (e.hasMoreElements()) {

            String name = (String) e.nextElement();
            String value = config.getInitParameter(name);

            RestClass restaurant = new RestClass(name, Integer.parseInt(value));

            restaurants.add(restaurant);

        }

    }

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

        if (action.equalsIgnoreCase("listRest")) {

            listRest(request, response);

        }

        if (action.equalsIgnoreCase("addRateRest")) {

            addRateRest(request, response);
        }

        if (action.equalsIgnoreCase("createRest")) {

            createRest(request, response);
        }

    }

    protected void listRest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try (PrintWriter out = response.getWriter()) {

            response.setCharacterEncoding("utf-8");

            JSONObject json = new JSONObject();
            JSONArray arrayRest = new JSONArray();

            for (RestClass rest : restaurants) {

                LinkedHashMap<String, String> jsonOrderedMap = new LinkedHashMap<String, String>();

                jsonOrderedMap.put("name", rest.getName());
                jsonOrderedMap.put("valoracio", rest.getValoracio().toString());
                jsonOrderedMap.put("mitjana", rest.getMitjana().toString());

                if (checkRatedRestSession(request, rest)) {

                    jsonOrderedMap.put("rated", "SI");

                } else {

                    jsonOrderedMap.put("rated", "NO");

                }

                JSONObject member = new JSONObject(jsonOrderedMap);
                arrayRest.put(member);

            }

            json.put("jsonArray", arrayRest);
            out.print(json.toString());
            out.close();

        } catch (JSONException e) {

        }

    }

    protected void addRateRest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try (PrintWriter out = response.getWriter()) {

            response.setCharacterEncoding("utf-8");

            String restaurant = request.getParameter("rest");
            String valoracio = request.getParameter("estels");

            for (RestClass rest : restaurants) {

                if (rest.getName().equalsIgnoreCase(restaurant)) {

                    rest.setValoracio(Integer.parseInt(valoracio));

                    addRatedRestToSession(request, rest);

                    JSONObject json = new JSONObject();

                    json.put("restaurantValorat", rest.getName());
                    json.put("valoracio", rest.getValoracio());
                    json.put("mitjanaRest", rest.getMitjana());

                    out.print(json.toString());
                    out.close();

                }

            }

        } catch (Exception e) {

        }

    }

    protected void createRest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nomRest = request.getParameter("fpname");
        String valIniRest = request.getParameter("fprate");
        Part p = request.getPart("fileImageName");
        String nomFitxer = p.getSubmittedFileName();

        //treiem la extenció del fitxer
        String nomFitxerSenseExt = nomFitxer.substring(0, nomFitxer.lastIndexOf("."));

        if (isValidFileName(nomRest, nomFitxerSenseExt)) {

            p.write(nomFitxer);
            //copiem el fitxer de la carpeta temp a la carpeta img
            File source = new File("C:\\Users\\USUARIO\\Desktop\\temp\\" + nomFitxer);
            String desti = getServletContext().getRealPath("\\") + "img\\" + nomFitxer;
            //File dest = new File("C:\\Users\\USUARIO\\Desktop\\IOC\\M07\\M07-EAC2\\DAWM07_EAC2_Aparicio_G\\src\\main\\webapp\\img\\" + nomFitxer);
            File dest = new File(desti);
            Files.copy(source.toPath(), dest.toPath(), REPLACE_EXISTING);

            RestClass restaurant = new RestClass(nomRest, Integer.parseInt(valIniRest));
            this.restaurants.add(restaurant);

            response.sendRedirect("index.html");

        } else {
            //si no es compleixen les validacions tornem a la pàgina d'inici index.html
            response.sendRedirect("index.html");
        }
    }

    private boolean isValidFileName(String nomRest, String fileName) {

        if (validation.isValidFileImageName(nomRest, fileName)) {

            for (RestClass rest : restaurants) {

                if (nomRest.equalsIgnoreCase(rest.getName())) {

                    return false;
                }
            }

            return true;
        }

        return false;

    }

    private void addRatedRestToSession(HttpServletRequest request, RestClass rest) {
        /*
         P3
         */

        try {

            RateLocal ratebean = (RateLocal) request.getSession().getAttribute("ratebean");

            if (ratebean == null) {

                ratebean = (RateLocal) new InitialContext().lookup("java:global/DAWM07_EAC2_Aparicio_G/Rate");
                ratebean.setRatedRest(new ArrayList<RestClass>());
                request.getSession().setAttribute("ratebean", ratebean);

            }

            ratebean.getRatedRest().add(rest);

        } catch (Exception e) {

        }

    }

    private Boolean checkRatedRestSession(HttpServletRequest request, RestClass rest) {

        try {

            RateLocal ratebean = (RateLocal) request.getSession().getAttribute("ratebean");

            if (ratebean == null) {

                ratebean = (RateLocal) new InitialContext().lookup("java:global/DAWM07_EAC2_Aparicio_G/Rate");
                ratebean.setRatedRest(new ArrayList<RestClass>());
                request.getSession().setAttribute("ratebean", ratebean);

            }
            
            for (RestClass restaurant: ratebean.getRatedRest()) {
                
                if (restaurant.getName().equalsIgnoreCase(rest.getName())) {
                    
                    return true;
                    
                }
            }


        } catch (Exception e) {

        }
        
        return false;
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
