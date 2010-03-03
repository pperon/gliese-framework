
package org.glieseframework.core;

import org.glieseframework.message.Message;


/** Common interface for any entity that can communicate. */
public interface Entity {

    /**  */
    String getName();

    /**  */
    void send(Message message);

}
