package org.muffin.muffin.servlets.movieowner.movieeditor;

import org.muffin.muffin.beans.Actor;
import org.muffin.muffin.beans.Character;
import org.muffin.muffin.beans.MovieOwner;
import org.muffin.muffin.responses.ResponseWrapper;
import org.muffin.muffin.servlets.EnsuredSessionServlet;
import org.muffin.muffin.servlets.MovieOwnerEnsuredSessionServlet;
import org.muffin.muffin.servlets.SessionKeys;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.muffin.muffin.daoimplementations.ActorDAOImpl;
import org.muffin.muffin.daoimplementations.CharacterDAOImpl;
import org.muffin.muffin.daoimplementations.MovieDAOImpl;
import org.muffin.muffin.daoimplementations.MovieOwnerDAOImpl;
import org.muffin.muffin.daos.ActorDAO;
import org.muffin.muffin.daos.CharacterDAO;
import org.muffin.muffin.daos.MovieDAO;
import org.muffin.muffin.daos.MovieOwnerDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

/**
 * doGetWithSession:  tries to create a new movie with given params, if success returns created obj, else returns error
 * doPostWithSession: same as GET
 */
@WebServlet("/movieowner/movieinfo/createchar")
public class CreateCharacter extends MovieOwnerEnsuredSessionServlet {
    ActorDAO actorDAO = new ActorDAOImpl();
    CharacterDAO characterDAO = new CharacterDAOImpl();

    @Override
    protected void doGetWithSession(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException {
        String characterName = request.getParameter("characterName");
        String actorName = request.getParameter("actorName");
        int movieId = Integer.parseInt(request.getParameter("movieId"));
        
        PrintWriter out = response.getWriter();
        Gson gson = new GsonBuilder().create();
        Optional<Actor> actorOpt = actorDAO.get(actorName);
        int error = 0;
        if (!actorOpt.isPresent()) {
        	
        	if (actorDAO.create(actorName) ){
                actorOpt = actorDAO.get(actorName);
                if (actorOpt.isPresent()) {
                    
                } else {
                	error = 1;
                    System.out.println("Critical error!");
                    out.println(gson.toJson(ResponseWrapper.error("Error!")));
                }
            } else {
            	error = 1;
                out.println(gson.toJson(ResponseWrapper.error("Error! Hint: The Actor name has to be different from all the existing ones")));
            }
        	
            
        }
        
        if(error == 0) {
        	
        	int actorId = actorOpt.get().getId();
            
            if (characterDAO.create(characterName, movieId, actorId)) {
                Optional<Character> characterOpt = characterDAO.get(characterName, movieId, actorId);
                if (characterOpt.isPresent()) {
                    out.println(gson.toJson(ResponseWrapper.get(characterOpt.get(), ResponseWrapper.OBJECT_RESPONSE)));
                } else {
                    System.out.println("Critical error!");
                    out.println(gson.toJson(ResponseWrapper.error("Error!")));
                }
            } else {
                out.println(gson.toJson(ResponseWrapper.error("Error! Hint: The Movie name has to be different from all the existing ones")));
            }
        	
        }
        
        
        out.close();
    }

    @Override
    protected void doPostWithSession(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException {
        doGetWithSession(request, response, session);
    }
}
