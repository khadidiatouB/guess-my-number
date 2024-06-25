package sn.esmt.guessNumber.web;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sn.esmt.guessNumber.exception.InvalidNumberException;

import java.util.Random;

@Controller


public class GuessNumberController {

    private int numberToGuess;
    private final Random random = new Random();

    public GuessNumberController() {
        this.numberToGuess = random.nextInt(100) + 1; // Génère un nombre entre 1 et 100
    }

    @GetMapping("/")
    public String getGuessNumber(HttpSession session, HttpServletRequest request, HttpServletResponse response, Model model) {

        // verifier si le cookie exist
        Cookie[] cookies = request.getCookies();
        boolean newGame = true;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("playing".equals(cookie.getName()) && "true".equals(cookie.getValue())) {
                    newGame = false;
                    break;
                }
            }
        }

        if (newGame) {
            this.numberToGuess = random.nextInt(100) + 1;
            session.setAttribute("attempts", 0);
            Cookie playingCookie = new Cookie("playing", "true");
            playingCookie.setMaxAge(3600); // 1 hour
            response.addCookie(playingCookie);
        }

        session.setAttribute("attempts", 0); // Initialiser les tentatives dans la session

        return "guessnumber";
    }

    @RequestMapping(value = "/", method=RequestMethod.POST)
    public String guessNumber(@ModelAttribute("number") int number, Model model, HttpSession session, HttpServletResponse response) throws InvalidNumberException {
        String contenu = "";

        int attempts = (int) session.getAttribute("attempts");
        attempts++;
        session.setAttribute("attempts", attempts);

        if (number < 1 || number > 100) {
            throw new InvalidNumberException();
        }

        if (number == numberToGuess) {

            contenu = "Félicitations! Vous avez trouvé le nombre en " + attempts + " tentatives.";

            session.setAttribute("attempts", 0); // Réinitialiser les tentatives après une victoire
            this.numberToGuess = random.nextInt(100) + 1; // Génère un nouveau nombre

            Cookie playingCookie = new Cookie("playing", "false");
            playingCookie.setMaxAge(0); // Faire expirer le cookie
            response.addCookie(playingCookie);
        }

        else if (number < numberToGuess) {
            contenu ="Trop bas! Essayez un nombre plus grand.";
        }

        else {
            contenu ="Trop haut! Essayez un nombre plus petit.";
        }

        System.out.println(contenu);

        model.addAttribute("message",contenu);

        return "guessnumber";
    }

    @ExceptionHandler(InvalidNumberException.class)
    public ResponseEntity<String> handleInvalidNumberException(InvalidNumberException ex) {
        return ResponseEntity.status(400).body(ex.getMessage());
    }

}
