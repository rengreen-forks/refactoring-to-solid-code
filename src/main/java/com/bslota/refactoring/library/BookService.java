package com.bslota.refactoring.library;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    @Autowired
    BookDAO bookDAO;

    @Autowired
    PatronDAO patronDAO;

    @Autowired
    NotificationSender emailService;

    boolean placeOnHold(int bookId, int patronId, int days) {
        Book book = bookDAO.getBookFromDatabase(bookId);
        Patron patron = patronDAO.getPatronFromDatabase(patronId);
        boolean flag = false;
        if (book != null && patron != null) {
            patron.placeOnHold(book, days);
            flag = true;
        }
        if (flag) {
            addLoyaltyPoints(patron);
        }
        if (flag && patron.isQualifiesForFreeBook()) {
            String title = "[REWARD] Free book waiting for you!";
            String body = "Dear Sir/Madame, \n" +
                    "we are pleased to inform you, that the number of loyalty points you have gathered " +
                    "is " + patron.getPoints() + ". \n" +
                    "It means we have a reward for you! A free book is waiting at your local library branch!";
            String email = patron.getEmail();
            emailService.sendMail(new String[]{email}, "contact@your-library.com", title, body);
        }
        return flag;
    }

    private void addLoyaltyPoints(Patron patron) {
        int type = patron.getType();
        switch (type) {
            case 0: // regular patron
                patron.setPoints(patron.getPoints() + 1);
                break;
            case 1:
                patron.setPoints(patron.getPoints() + 5);
                break;
            case 2:
                int newPoints;
                if (patron.getPoints() == 0) {
                    newPoints = 100;
                } else {
                    newPoints = patron.getPoints() * 2;
                }
                patron.setPoints(newPoints);
                break;
            default:
                break;
        }
        if (patron.getPoints() > 10000) {
            patron.setQualifiesForFreeBook(true);
        }
    }

}
