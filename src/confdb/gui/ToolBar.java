package confdb.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/**
 * ToolBar
 * -------
 * @author Philipp Schieferdecker
 */
public class ToolBar
{
    //
    // member data
    //
    
    /** the tool bar */
    private JToolBar jToolBar = null;

    /** action commands for toolbar buttons */
    private static final String cmdNew         = "New";
    private static final String cmdParse       = "Parse";
    private static final String cmdOpen        = "Open";
    private static final String cmdClose       = "Close";
    private static final String cmdSave        = "Save";
    private static final String cmdCommentSave = "Comment&Save";
    private static final String cmdSaveAs      = "Save As";
    private static final String cmdImport      = "Import";
    private static final String cmdMigrate     = "Migrate";
    private static final String cmdConvert     = "Convert";
    
    private static final String cmdConnectToDB      = "Connect to DB";
    private static final String cmdDisconnectFromDB = "Disconnect from DB";
    private static final String cmdExportConfig     = "Export Configuration";
    
    /** toolbar buttons */
    private JButton jButtonNew         = new JButton();
    private JButton jButtonClose       = new JButton();
    
    private JButton jButtonOpen        = new JButton();
    private JButton jButtonSave        = new JButton();
    private JButton jButtonSaveAs      = new JButton();
    
    private JButton jButtonImport      = new JButton();
    private JButton jButtonMigrate     = new JButton();
    private JButton jButtonConvert     = new JButton();
    
    private JButton jButtonConnect     = new JButton();
    private JButton jButtonDisconnect  = new JButton();
    private JButton jButtonExport      = new JButton();
    
    

    //
    // construction
    //

    /** standard constructor */
    public ToolBar(JToolBar jToolBar,ConfDbGUI app)
    {
	this.jToolBar = jToolBar;
	populateToolBar(new CommandActionListener(app));
    }
    
    
    //
    // memeber functions
    //
    
    /** a configuration was opened */
    public void configurationIsOpen()
    {
	dbConnectionIsEstablished();
	jButtonNew.setEnabled(true);
	jButtonOpen.setEnabled(true);
	jButtonClose.setEnabled(true);
	jButtonSave.setEnabled(true);
	jButtonSaveAs.setEnabled(true);
	jButtonImport.setEnabled(true);
	jButtonMigrate.setEnabled(true);
	jButtonConvert.setEnabled(true);
	jButtonExport.setEnabled(true);
    }

    /** no configuration is open */
    public void configurationIsNotOpen()
    {
	jButtonClose.setEnabled(false);
	jButtonSave.setEnabled(false);
	jButtonSaveAs.setEnabled(false);
	jButtonImport.setEnabled(false);
	jButtonMigrate.setEnabled(false);
	jButtonConvert.setEnabled(false);
	jButtonExport.setEnabled(false);
    }
    
    /** database connection is established */
    public void dbConnectionIsEstablished()
    {
	jButtonNew.setEnabled(true);
	jButtonOpen.setEnabled(true);
	jButtonDisconnect.setEnabled(true);
    }
    
    /** no database connection is established */
    public void dbConnectionIsNotEstablished()
    {
	configurationIsNotOpen();
	jButtonNew.setEnabled(false);
	jButtonOpen.setEnabled(false);
	jButtonDisconnect.setEnabled(false);
    }

    /** populate the menu bar with all menus and their items */
    private void populateToolBar(CommandActionListener listener)
    {
	jButtonNew.setActionCommand(cmdNew);
	jButtonNew.addActionListener(listener);
	jButtonNew.setToolTipText("create a new configuration");
	jButtonNew.setIcon(new ImageIcon(getClass().
					 getResource("/NewIcon.png")));
	jToolBar.add(jButtonNew);

	jButtonClose.setActionCommand(cmdClose);
	jButtonClose.addActionListener(listener);
	jButtonClose.setToolTipText("close the current configuration");
	jButtonClose.setIcon(new ImageIcon(getClass().
					   getResource("/CloseIcon.png")));
	jToolBar.add(jButtonClose);
	
	jToolBar.addSeparator();
	
	jButtonOpen.setActionCommand(cmdOpen);
	jButtonOpen.addActionListener(listener);
	jButtonOpen.setToolTipText("open an existing configuration");
	jButtonOpen.setIcon(new ImageIcon(getClass().
					  getResource("/OpenIcon.png")));
	jToolBar.add(jButtonOpen);
	
	jButtonSave.setActionCommand(cmdCommentSave);
	jButtonSave.addActionListener(listener);
	jButtonSave.setToolTipText("save new version of current configuration");
	jButtonSave.setIcon(new ImageIcon(getClass().
					  getResource("/SaveIcon.png")));
	jToolBar.add(jButtonSave);
	
	//jButtonSaveAs.setActionCommand(cmdSaveAs);
	//jButtonSaveAs.addActionListener(listener);
	//jButtonSaveAs.setToolTipText("save current configuration under a new name");
	//jButtonSaveAs.setIcon(new ImageIcon(getClass().
	//			    getResource("/SaveAsIcon.png")));
	//jToolBar.add(jButtonSaveAs);
	
	jToolBar.addSeparator();
	
	jButtonImport.setActionCommand(cmdImport);
	jButtonImport.addActionListener(listener);
	jButtonImport.setToolTipText("import components from another configuration");
	jButtonImport.setIcon(new ImageIcon(getClass().
					    getResource("/ImportIcon.png")));
	jToolBar.add(jButtonImport);
	
	jButtonMigrate.setActionCommand(cmdMigrate);
	jButtonMigrate.addActionListener(listener);
	jButtonMigrate.setToolTipText("migrate configuration to another release");
	jButtonMigrate.setIcon(new ImageIcon(getClass().
					     getResource("/MigrateIcon.png")));
	jToolBar.add(jButtonMigrate);
	
	jButtonConvert.setActionCommand(cmdConvert);
	jButtonConvert.addActionListener(listener);
	jButtonConvert.setToolTipText("convert configuration to a file");
	jButtonConvert.setIcon(new ImageIcon(getClass().
					     getResource("/ConvertIcon.png")));
	jToolBar.add(jButtonConvert);
	
	jToolBar.addSeparator();
	
	jButtonConnect.setActionCommand(cmdConnectToDB);
	jButtonConnect.addActionListener(listener);
	jButtonConnect.setToolTipText("connect to database");
	jButtonConnect.setIcon(new ImageIcon(getClass().
					     getResource("/ConnectIcon.png")));
	jToolBar.add(jButtonConnect);
	
	jButtonDisconnect.setActionCommand(cmdDisconnectFromDB);
	jButtonDisconnect.addActionListener(listener);
	jButtonDisconnect.setToolTipText("disconnect from database");
	jButtonDisconnect.setIcon(new ImageIcon(getClass().
						getResource("/DisconnectIcon.png")));
	jToolBar.add(jButtonDisconnect);
	
	
	jButtonExport.setActionCommand(cmdExportConfig);
	jButtonExport.addActionListener(listener);
	jButtonExport.setToolTipText("export configuration to another database");
	jButtonExport.setIcon(new ImageIcon(getClass().
					    getResource("/ExportIcon.png")));
	jToolBar.add(jButtonExport);
    }
    

}