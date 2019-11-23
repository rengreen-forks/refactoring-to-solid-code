package com.bslota.refactoring.library;

import org.junit.jupiter.api.Test;

import static com.bslota.refactoring.library.BookFixture.availableBook;
import static com.bslota.refactoring.library.PatronFixture.PatronBuilder.newPatron;
import static com.bslota.refactoring.library.PatronFixture.patronWithMaxNumberOfHolds;
import static com.bslota.refactoring.library.PatronFixture.patronWithoutHolds;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author bslota on 23/11/2019
 */
class PatronTest {

    private static final int PATRON_ID_VALUE = 10;
    private static final int BOOK_ID_VALUE = 1;

    @Test
    void shouldCreateWithGivenId() {
        //given
        PatronId id = PatronId.of(PATRON_ID_VALUE);

        //when
        Patron patron = newPatron().withId(id).build();

        //then
        assertEquals(PATRON_ID_VALUE, patron.getPatronIdValue());
        assertEquals(id, patron.getPatronId());
    }

    @Test
    void shouldPlaceBookOnHold() {
        //given
        Book book = availableBook();
        Patron patron = patronWithoutHolds();

        //when
        patron.placeOnHold(book);

        //then
        assertEquals(1, patron.getHolds().size());
    }

    @Test
    void shouldReturnSuccessWhenBookIsPlacedOnHold() {
        //given
        BookId bookId = BookId.of(BOOK_ID_VALUE);
        Patron patron = patronWithoutHolds();

        //when
        PlaceOnHoldResult result = patron.placeOnHold(bookId);

        //then
        assertTrue(result instanceof BookPlacedOnHold);
        assertEquals(patron.getPatronId(), result.patronId());
        assertEquals(bookId, result.bookId());
    }

    @Test
    void shouldReturnFailureWhenPatronHasReachedMaxNumberOfHolds() {
        //given
        BookId bookId = BookId.of(BOOK_ID_VALUE);
        Patron patron = patronWithMaxNumberOfHolds();

        //when
        PlaceOnHoldResult result = patron.placeOnHold(bookId);

        //then
        assertTrue(result instanceof PlacingOnHoldFailed);
        assertEquals(patron.getPatronId(), result.patronId());
        assertEquals(bookId, result.bookId());
    }
}