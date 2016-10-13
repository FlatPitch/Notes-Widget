
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

/**
 *
 * @author brendan
 */
public class NotesWidgetUI extends JDialog implements ActionListener{
    
    private JPanel buttonPanel;
    private JButton addNewNoteBtn;
    private JButton removeNoteBtn;
    private ArrayList<JTextField> notes;
    
    public NotesWidgetUI(){
        initCompoents();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width * 3/4), screenSize.height * 1/4);
        this.setUndecorated(true);
        this.setResizable(false);
        this.setVisible(true);
        this.pack();
    }
    
    private void initCompoents() {
        //Assigning components
        buttonPanel = new JPanel();
        addNewNoteBtn = new JButton("Add Note");
        removeNoteBtn = new JButton("Remove Note");
        notes = new ArrayList<>();
        
        readNotesFromFile();
        
        addNewNoteBtn.addActionListener(this);
        removeNoteBtn.addActionListener(this);
       
        //Building UI
        buttonPanel.add(addNewNoteBtn);
        buttonPanel.add(removeNoteBtn);
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        
        this.add(buttonPanel);
        
    }
    
    private JTextField createNoteSpace(){
        JTextField noteSpace = new JTextField(25);
        Font aFont = new Font("Arial", Font.PLAIN, 20);
        noteSpace.setFont(aFont);
        noteSpace.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "saveNotes");
        noteSpace.getActionMap().put("saveNotes", new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e) {
                writeNotesToFile();
            }
            
        });
        return noteSpace;
    }
    
    private JTextField createNoteSpace(String note){
        JTextField noteSpace = new JTextField(note,25);
        Font aFont = new Font("Arial", Font.PLAIN, 20);
        noteSpace.setFont(aFont);
        return noteSpace;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addNewNoteBtn){
            notes.add(createNoteSpace());

            this.remove(buttonPanel);
            this.add(notes.get(notes.size()-1));
            notes.get(notes.size()-1).requestFocus();
            this.add(buttonPanel);
        }
        else{
            if (notes.size() >= 1){
                this.remove(notes.get(notes.size()-1));
                notes.remove(notes.size()-1);
            }
        }
        writeNotesToFile();
        this.pack();
        
    }
    
    private void writeNotesToFile() {
                try {
                    PrintWriter writer = new PrintWriter(new FileOutputStream("notes.txt",false));
                    
                    for (JTextField eachNote: notes){
                        if (!eachNote.getText().equals("")){
                            writer.println(eachNote.getText());
                        }
                    }
                    writer.close();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(NotesWidgetUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
    
     private void readNotesFromFile() {
                try {
                    BufferedReader reader = new BufferedReader(new FileReader("notes.txt"));
                    
                    String line = reader.readLine();
                    while (line != null){
                        notes.add(createNoteSpace(line));
                        this.remove(buttonPanel);
                        this.add(notes.get(notes.size()-1));
                        this.add(buttonPanel);
                        line = reader.readLine();
                    }
                    this.revalidate();
                    reader.close();
                } catch (FileNotFoundException ex) {
                } catch (IOException ex) {
                }
            }
    
    public static void main(String [] args){
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){
             NotesWidgetUI widget = new NotesWidgetUI();
            }
        });
    }
}
