package dev.icheppy.aotf.utils;

import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.event.HoverEvent.Action;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;


public class MessageBuilder {

    private final IChatComponent parent;

    private String text;
    private ChatStyle style;

    private MessageBuilder(String text) {
        this(text, null, Inheritance.SHALLOW);
    }

    private MessageBuilder(String text, IChatComponent parent, Inheritance inheritance) {
        this.parent = parent;
        this.text = text;

        switch (inheritance) {
            case DEEP:
                this.style = parent != null ? parent.getChatStyle() : new ChatStyle();
                break;
            default:
            case SHALLOW:
                this.style = new ChatStyle();
                break;
            case NONE:
                this.style = new ChatStyle().setColor(null).setBold(false).setItalic(false)
                        .setStrikethrough(false).setUnderlined(false).setObfuscated(false)
                        .setChatClickEvent(null).setChatHoverEvent(null).setInsertion(null);
                break;
        }
    }

    public static MessageBuilder of(String text) {
        return new MessageBuilder(text);
    }

    public MessageBuilder setColor(EnumChatFormatting color) {
        style.setColor(color);
        return this;
    }

    public MessageBuilder setBold(boolean bold) {
        style.setBold(bold);
        return this;
    }

    public MessageBuilder setItalic(boolean italic) {
        style.setItalic(italic);
        return this;
    }

    public MessageBuilder setStrikethrough(boolean strikethrough) {
        style.setStrikethrough(strikethrough);
        return this;
    }

    public MessageBuilder setUnderlined(boolean underlined) {
        style.setUnderlined(underlined);
        return this;
    }

    public MessageBuilder setObfuscated(boolean obfuscated) {
        style.setObfuscated(obfuscated);
        return this;
    }

    public MessageBuilder setClickEvent(ClickEvent.Action action, String value) {
        style.setChatClickEvent(new ClickEvent(action, value));
        return this;
    }

    public MessageBuilder setHoverEvent(String value) {
        return this.setHoverEvent(new ChatComponentText(value));
    }

    public MessageBuilder setHoverEvent(IChatComponent value) {
        return this.setHoverEvent(Action.SHOW_TEXT, value);
    }

    public MessageBuilder setHoverEvent(HoverEvent.Action action, IChatComponent value) {
        style.setChatHoverEvent(new HoverEvent(action, value));
        return this;
    }

    public MessageBuilder setInsertion(String insertion) {
        style.setInsertion(insertion);
        return this;
    }

    public MessageBuilder append(String text) {
        return this.append(text, Inheritance.SHALLOW);
    }

    public MessageBuilder append(String text, Inheritance inheritance) {
        return new MessageBuilder(text, this.build(), inheritance);
    }

    public IChatComponent build() {
        IChatComponent thisComponent = new ChatComponentText(text).setChatStyle(style);
        return parent != null ? parent.appendSibling(thisComponent) : thisComponent;
    }

    public enum Inheritance {
        DEEP, SHALLOW, NONE
    }

}