import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.JBColor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class ShowQrAction extends AnAction {

    public void actionPerformed(AnActionEvent event) {
        Editor editor = event.getData(PlatformDataKeys.EDITOR);

        String text;

        try {
            text = editor.getSelectionModel().getSelectedText().trim();

        } catch (NullPointerException e) {
            return;
        }

        if (!text.isEmpty()) {

            String height = String.valueOf((int)(editor.getComponent().getHeight() / 1.25));

            String tpl = (
                "<img src=\"" +
                    "https://api.qrserver.com/v1/create-qr-code/?size=250x250&qzone=2&data={put}\" " +
                "width=\"" + height + "\" height=\"" + height + "\" alt=\"" + text + "\">"
            );

            String textEncoded;

            try {
                textEncoded = tpl.replace("{put}", URLEncoder.encode(text, "UTF-8"));

            } catch (UnsupportedEncodingException e) {
                throw new AssertionError("UTF-8 not supported for URL encoding");
            }

            JBPopupFactory.getInstance()
                    .createHtmlTextBalloonBuilder(textEncoded, null, JBColor.WHITE, null)
                    .setShowCallout(false)
                    .createBalloon()
                    .showInCenterOf(editor.getComponent());
        }
    }
}
