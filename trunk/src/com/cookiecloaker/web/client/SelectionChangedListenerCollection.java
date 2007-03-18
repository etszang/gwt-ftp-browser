/*
 * 
 */
package com.cookiecloaker.web.client;

import java.util.Iterator;
import java.util.Vector;

import com.google.gwt.user.client.ui.Widget;

/**
 * A helper class for implementers of the SourcesClickEvents interface. This
 * subclass of Vector assumes that all objects added to it will be of type
 * {@link com.google.gwt.user.client.ui.ClickListener}.
 */
public class SelectionChangedListenerCollection extends Vector {

  /**
   * Fires a click event to all listeners.
   * 
   * @param sender the widget sending the event.
   */
  public void fireClick(String token, Widget sender) {
    for (Iterator it = iterator(); it.hasNext();) {
      SelectionChangedListener listener = (SelectionChangedListener) it.next();
      listener.onSelectionChanged(token, sender);
    }
  }
}
