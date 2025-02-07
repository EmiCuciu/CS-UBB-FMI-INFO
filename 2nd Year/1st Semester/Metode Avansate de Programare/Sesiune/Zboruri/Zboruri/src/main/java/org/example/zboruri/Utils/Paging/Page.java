package org.example.zboruri.Utils.Paging;

public class Page<E> {
    private Iterable<E> elementsOnPage;
    private int totalNumberOfElements;

    public Page(Iterable<E> elementsOnPage, int totalNumberOfElements) {
        this.elementsOnPage = elementsOnPage;
        this.totalNumberOfElements = totalNumberOfElements;
    }

    public Iterable<E> getElementsOnPage() {
        return elementsOnPage;
    }

    public int getTotalNumberOfElements() {
        return totalNumberOfElements;
    }
}