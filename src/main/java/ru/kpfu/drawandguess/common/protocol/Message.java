package ru.kpfu.drawandguess.common.protocol;

import java.io.Serializable;

public interface Message extends Serializable {

    MessageType getType();

}
