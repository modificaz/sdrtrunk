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
package alias.id.lojack;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

import module.decode.lj1200.LJ1200Message;
import module.decode.lj1200.LJ1200Message.Function;
import net.miginfocom.swing.MigLayout;
import alias.AliasID;
import alias.ComponentEditor;

public class LoJackIDEditor extends ComponentEditor<AliasID>
{
    private static final long serialVersionUID = 1L;
    
    private JTextField mTextField;
    
    private JComboBox<LJ1200Message.Function> mFunctionCombo;

    private static final String HELP_TEXT = "<html>"
    		+ "<h3>LoJack Example</h3>"
    		+ "<b>Function Code:</b> <u>1Y-SITE ID</u><br>"
    		+ "<b>ID:</b> 5 numbers or characters (e.g. <u>1BN47</u>)<br>"
            + "<br>"
            + "<b>Wildcard:</b> use an asterisk (*) to wildcard ID<br>"
            + "characters (e.g. <u>AB*CD</u> or <u>***12</u> or <u>*****</u>)<br><br>"
            + "The middle character in a reply ID code identifies the<br>"
            + "entity.  Valid ID middle characters are:<br>"
            + "<li><b>Tower:</b> X,Y</li>"
            + "<li><b>Transponder:</b> 0-9,A,C-H,J-N,P-W</li>"
            + "<li><b>Not Used:</b> B,I,O,Z</li>"
    		+ "</html>";

    public LoJackIDEditor( AliasID aliasID )
	{
		super( aliasID );
		
		initGUI();
		
		setComponent( aliasID );
	}
	
	private void initGUI()
	{
		setLayout( new MigLayout( "fill,wrap 2", "[right][left]", "[][][]" ) );

		add( new JLabel( "Function:" ) );
		mFunctionCombo = new JComboBox<LJ1200Message.Function>( Function.values() );
		mFunctionCombo.addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed( ActionEvent e )
			{
				setModified( true );
			}
		} );
		mFunctionCombo.setToolTipText( HELP_TEXT );
		add( mFunctionCombo, "growx, push" );
		
		add( new JLabel( "ID:" ) );
		
		MaskFormatter formatter = null;

		try
		{
			//Mask: any character or number, 5 places
			formatter = new MaskFormatter( "AAAAA" );
		}
		catch( Exception e )
		{
			//Do nothing, the mask was invalid
		}
		
		mTextField = new JFormattedTextField( formatter );
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
				JOptionPane.showMessageDialog( LoJackIDEditor.this, 
					HELP_TEXT, "Example", JOptionPane.INFORMATION_MESSAGE );
			}
		} );
		add( example );
	}
	
	public LoJackFunctionAndID getLoJackID()
	{
		if( getComponent() instanceof LoJackFunctionAndID )
		{
			return (LoJackFunctionAndID)getComponent();
		}
		
		return null;
	}

	@Override
	public void setComponent( AliasID aliasID )
	{
		mComponent = aliasID;
		
		LoJackFunctionAndID lojack = getLoJackID();
		
		if( lojack != null )
		{
			mFunctionCombo.setSelectedItem( lojack.getFunction() );
			mTextField.setText( lojack.getID() );
		}
		else
		{
			mFunctionCombo.setSelectedItem( null );
			mTextField.setText( null );
		}
		
		setModified( false );
		
		repaint();
	}

	@Override
	public void save()
	{
		LoJackFunctionAndID lojack = getLoJackID();
		
		if( lojack != null )
		{
			lojack.setID( mTextField.getText() );
			lojack.setFunction( (Function)mFunctionCombo.getSelectedItem() );
		}
		
		setModified( false );
	}
}
