package edu.coms.sr2.game.interfacing.views;

public interface Button extends TextView {
    void onClick(Runnable r);
    boolean isClickable();
    Button setClickable(boolean val);
//
//    class GenericImplementation implements Button {
//        private Runnable clickAction;
//        public GenericImplementation(Runnable clickAction) {
//            this.clickAction = clickAction;
//        }
//        @Override
//        public void onClick(Runnable r) {
//            this.clickAction = clickAction;
//        }
//        public Runnable getClickAction() {
//            return clickAction;
//        }
//    }
}
