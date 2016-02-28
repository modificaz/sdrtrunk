/*******************************************************************************
 *     SDR Trunk 
 *     Copyright (C) 2014-2016 Dennis Sheirer
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>
 ******************************************************************************/
package alias.id.esn;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import alias.AliasID;
import alias.ComponentEditor;
import alias.id.talkgroup.TalkgroupIDEditor;

public class ESNEditor extends ComponentEditor<AliasID>
{
    private static final long serialVersionUID = 1L;

    private static final String HELP_TEXT = 
    		"<html><h3>Electronic Serial Number (ESN) Example</h3>"
    		+ "<b>ESN:</b> hexadecimal 0-9, A-F (e.g. <u>ABCD1234</u> )<br>"
    		+ "<b>Wildcard:</b> use an asterisk (*) to wildcard individual<br>"
    		+ "digits (e.g. <u>ABCD123*</u> or <u>AB**1**4</u>)"
    		+ "</html>";

    private JTextField mTextField;

	public ESNEditor( AliasID aliasID )
	{
		super( aliasID );
		
		initGUI();
		
		setComponent( aliasID );
	}
	
	private void initGUI()
	{
		setLayout( new MigLayout( "fill,wrap 2", "[right][left]", "[][]" ) );

		add( new JLabel( "ESN:" ) );
		mTextField = new JTextField();
		mTextField.getDocument().addDocumentListener( this );
		mTextField.setToolTipText( HELP_TEXT );
		add( mTextField, "growx,push" );

		JLabel example = new JLabel( "Example ..." );
		example.setForeground( Color.BLUE.brighter() );
		example.setCursor( new Cursor( Cursor.HAND_CURSOR ) );
		example.addMouseListener( new MouseAdapter() 
		{
			@Override
			public void mouseClicked( MouseEvent e )
			{
				JOptionPane.showMessageDialog( ESNEditor.this, 
					HELP_TEXT, "Example", JOptionPane.INFORMATION_MESSAGE );
			}
		} );
		add( example );
	}
	
	public Esn getEsn()
	{
		if( getComponent() instanceof Esn )
		{
			return (Esn)getComponent();
		}
		
		return null;
	}

	@Override
	public void setComponent( AliasID aliasID )
	{
		mComponent = aliasID;
		
		Esn esn = getEsn();
		
		if( esn != null )
		{
			mTextField.setText( esn.getEsn() );
		}
		
		setModified( false );
		
		repaint();
	}

	@Override
	public void save()
	{
		Esn esn = getEsn();
		
		if( esn != null )
		{
			esn.setEsn( mTextField.getText() );
		}
		
		setModified( false );
	}
}
