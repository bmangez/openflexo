/*
 * PreviewHyperlinkExample.java
 *
 *
 */

/**
 *
 * @author  Vassil Boyadjiev
 */
public class EnablingBrowsingInPreview extends javax.swing.JFrame{

    /** Creates new form EnablingBrowsingInPreview */
    public EnablingBrowsingInPreview() {
        initComponents();
        hTMLEditor1.setBrowsingInPreviewEnabled(true);
        hTMLEditor1.setVisualEditorFollowsPreview(true);

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        hTMLEditor1 = new sferyx.administration.editors.HTMLEditor();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        getContentPane().add(hTMLEditor1, java.awt.BorderLayout.CENTER);

        pack();
    }//GEN-END:initComponents

    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        EnablingBrowsingInPreview frame=new EnablingBrowsingInPreview();
        frame.show();
        frame.setLocation(100,100);
        frame.setSize(800,600);
    }

  

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private sferyx.administration.editors.HTMLEditor hTMLEditor1;
    // End of variables declaration//GEN-END:variables

}
