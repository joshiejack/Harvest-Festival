package uk.joshiejack.penguinlib.scripting.wrappers;

public class AbstractJS<C>  {
    public C penguinScriptingObject;

    public AbstractJS(C object) {
        this.penguinScriptingObject = object;
    }
}
