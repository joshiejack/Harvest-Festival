package uk.joshiejack.harvestcore.client.gui.button;

import uk.joshiejack.penguinlib.client.gui.book.page.PageMultiple;
import org.apache.logging.log4j.util.Strings;

public class PageSearchable {
    public abstract static class Multiple<R> extends PageMultiple<R> {
        protected String search = Strings.EMPTY;

        public Multiple(String name, int itemsPerPage) {
            super(name, itemsPerPage);
        }

        public String getSearch() {
            return search;
        }
    }

    public abstract static class MultipleButton<R> extends PageMultiple.PageMultipleButton<R> {
        protected String search = Strings.EMPTY;

        public MultipleButton(String name, int itemsPerPage) {
            super(name, itemsPerPage);
        }

        public String getSearch() {
            return search;
        }
    }
}
