package org.muffin.muffin.servlets.review;

import com.google.gson.GsonBuilder;
import org.muffin.muffin.beans.Review;
import org.muffin.muffin.daoimplementations.ReviewDAOImpl;
import org.muffin.muffin.daos.ReviewDAO;
import org.muffin.muffin.responses.ResponseWrapper;
import org.muffin.muffin.servlets.EnsuredSessionServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * doGetWithSession:  returns requested reviews
 * doPostWithSession: same as get
 */
@WebServlet("/review/fetch/followers")
public class FetchByFollowers extends EnsuredSessionServlet {
    ReviewDAO reviewDAO = new ReviewDAOImpl();

    @Override
    protected void doGetWithSession(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException {
        int muffId = Integer.parseInt(request.getParameter("muffId"));
        int offset = Integer.parseInt(request.getParameter("offset"));
        int limit = Integer.parseInt(request.getParameter("limit"));
        List<Review> reviews = reviewDAO.getByFollowers(muffId, offset, limit);
        PrintWriter out = response.getWriter();
        out.println(new GsonBuilder().create().toJson(ResponseWrapper.get(reviews, ResponseWrapper.ARRAY_RESPONSE)));
        out.close();
    }

    @Override
    protected void doPostWithSession(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException {
        doGetWithSession(request, response, session);
    }
}
