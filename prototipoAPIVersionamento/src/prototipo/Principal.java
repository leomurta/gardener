/*
 * Autor: Evaldo de Oliveira da Silva
 * Descrição: Protótipo para modelo de versionamento do sistema Gardener
 */
package prototipo;

import java.io.*;
import java.io.FileInputStream.*;
import java.io.DataInputStream.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.io.File;
import java.util.*;

import Versioning.Versioning;

public class Principal extends JPanel implements ActionListener
{

    //streams
    FileInputStream origem;
    FileOutputStream destino;

    //canais
    FileChannel fcOrigem;
    FileChannel fcDestino;
   
    static private final String newline = "\n";

    JButton openButton
          , cancelButton
          , sendButton
          , consultaButton
          , origemButton
          , destinoButton
          , criaProjetoButton
          , proximaVersaoButton;
    
    JTextField comentarioField
             , usuarioField
             , dataField
             , consultaField
             , origemField
             , destinoField
             , projetoField;

    JLabel comentarioLabel
         , usuarioLabel
         , dataLabel
         , consultaLabel
         , projetoLabel;

    JTextArea log;
    JFileChooser fc;

    public Principal()
    {
	super(new BorderLayout());

	//Parte de info aqui.
	log = new JTextArea(15, 40);
	log.setMargin(new Insets(5, 5, 5, 5));
	log.setEditable(false);
	JScrollPane logScrollPane = new JScrollPane(log);

	fc = new JFileChooser();

	openButton = new JButton("Checkout"); 
	openButton.addActionListener(this);
	
	sendButton = new JButton("Checkin");
	sendButton.addActionListener(this);
	
	cancelButton = new JButton("Cancelar");
	cancelButton.addActionListener(this);

        consultaButton = new JButton("Consultar");
        consultaButton.addActionListener(this);

        criaProjetoButton = new JButton("Criar Projeto");
        criaProjetoButton.addActionListener(this);

        origemButton = new JButton("Diretorio Origem");
	origemButton.addActionListener(this);

        destinoButton = new JButton("Diretorio Destino");
	destinoButton.addActionListener(this);

        proximaVersaoButton = new JButton("Proxima Versao do Projeto");
	proximaVersaoButton.addActionListener(this);
	       
        comentarioLabel = new JLabel("Log: ");
        usuarioLabel = new JLabel("Usuario: ");
        dataLabel = new JLabel("Data: ");
        consultaLabel = new JLabel("Consulta por revisao: ");
        projetoLabel = new JLabel("Projeto: ");

        comentarioField = new JTextField(20);
        usuarioField = new JTextField(25);
        dataField = new JTextField(10);
        consultaField = new JTextField(3);
        origemField = new JTextField(10);
        destinoField = new JTextField(10);         
        projetoField = new JTextField(15);


        JPanel camposPanel = new JPanel();        
        camposPanel.add(comentarioLabel);
        camposPanel.add(comentarioField);
        camposPanel.add(usuarioLabel);
        camposPanel.add(usuarioField);
        camposPanel.add(dataLabel);
        camposPanel.add(dataField);
        camposPanel.add(projetoLabel);
        camposPanel.add(projetoField);
        camposPanel.add(consultaLabel);
        camposPanel.add(consultaField);

	JPanel buttonPanel = new JPanel();
        buttonPanel.add(sendButton);
        buttonPanel.add(consultaButton);
	buttonPanel.add(cancelButton);
        buttonPanel.add(criaProjetoButton);
        buttonPanel.add(proximaVersaoButton);

	//Adicionando os recursos no painel.                
        add(logScrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.PAGE_END);
        add(camposPanel, BorderLayout.PAGE_START);        
    }

    public void actionPerformed(ActionEvent e)
    {
	//Acão nos botões.
	 if(e.getSource() == sendButton)
            {
                int returnVal = fc.showOpenDialog(Principal.this);

                if(returnVal == JFileChooser.APPROVE_OPTION)
                {
                    File file = fc.getSelectedFile();                    

                    try
                    {                      

                        Versioning revision = new Versioning();

                        revision.intPort = 27017;
                        revision.stgServer = "localhost";

                        revision.stgComentario = comentarioField.getText();
                        revision.stgData = dataField.getText();
                        revision.stgProject = projetoField.getText();                        
                        revision.stgUsuario = usuarioField.getText();                        

                        revision.createRevision(revision.stgProject);
                        revision.createFileRevision(revision.stgProject, file.getAbsolutePath(), file.getName());

                        comentarioField.setText("");
                        usuarioField.setText("");
                        dataField.setText("");
                        projetoField.setText("");
                        consultaField.setText("");

                    }

                    catch(Exception e1)
                    {
                        log.append("Erro ao realizar o checkin do arquivo: " + e1.getMessage() + newline);
                        log.invalidate();
                        return;
                    }


                } else{
                    log.append("Usuário desistiu de realizar o ckeckin do arquivo." + newline);
                }

                log.setCaretPosition(log.getDocument().getLength());
            }
           else if(e.getSource() == cancelButton)
            {              

		System.exit(0);
            }
          else if(e.getSource() == origemButton)
            {
                try
                    {
                        int returnVal = fc.showOpenDialog(Principal.this);

                        if(returnVal == JFileChooser.APPROVE_OPTION)
                        {
                            File file = fc.getSelectedFile();

                            origemField.setText(file.getAbsolutePath());

                        } else{
                            log.append("Usuário desistiu de realizar a escolha do arquivo." + newline);
                        }
                    }
                  catch(Exception e1)
                    {
                        log.append("Erro ao realizar a escolha do diretório de origem: " + e1.getMessage() + newline);
                        log.invalidate();
                        return;
                    }
             }
           else if(e.getSource() == destinoButton)
            {
                try
                    {
                        int returnVal = fc.showOpenDialog(Principal.this);

                        if(returnVal == JFileChooser.APPROVE_OPTION)
                        {
                            File file = fc.getSelectedFile();

                            destinoField.setText(file.getAbsolutePath());

                        } else{
                            log.append("Usuário desistiu de realizar a escolha do arquivo." + newline);
                        }
                    }
                  catch(Exception e1)
                    {
                        log.append("Erro ao realizar a escolha do diretório de origem: " + e1.getMessage() + newline);
                        log.invalidate();
                        return;
                    }
             }
           else if(e.getSource() == consultaButton)
            {
                   try
                    {

                        if (projetoField.getText().isEmpty() || consultaField.getText().isEmpty())
                        {
                            JOptionPane.showMessageDialog(null, "Campos de projeto ou consulta por versão podem estar vazios!", "Protótipo do Sistema Gardener", JOptionPane.INFORMATION_MESSAGE);
                        }

                        Versioning revision = new Versioning();

                        File[] listFile;

                        log.setText("");

                        revision.intPort = 27017;
                        revision.stgServer = "localhost";
                        revision.stgProject = projetoField.getText(); 
                        revision.stgRevisao = consultaField.getText();

                        ArrayList listRevision = revision.metadataRevision();

                        for (int i = 0; i < listRevision.size(); i++) {                            
                            log.append(listRevision.get(i) + "" + newline);
                        }
                        
                        listFile = revision.listRevisionFiles(revision.stgProject, Integer.parseInt(revision.stgRevisao));

                        for (int i = 0; i < listFile.length; i++) {
                            log.append(listFile[i].getName() + "" + newline);
                        }

                        projetoField.setText("");
                        consultaField.setText("");

                        //GridFS arqRevisao = new GridFS(conn);
                        //log.append(arqRevisao.findOne(consultaField.getText()).toString());
                         

                   }
                  catch(Exception e1)
                    {
                        log.append("Erro ao realizar a consulta: " + e1.getMessage() + newline);
                        log.invalidate();
                        return;
                    }
            }
         else if(e.getSource() == criaProjetoButton)
            {
                   try
                    {

                       Versioning revision = new Versioning();
                       revision.createRevisionProject(projetoField.getText());

                       JOptionPane.showMessageDialog(null, "Projeto criado com sucesso!", "Protótipo do Sistema Gardener", JOptionPane.INFORMATION_MESSAGE);

                       log.setText("");
                       projetoField.setText("");

                   }
                  catch(Exception e1)
                    {
                        log.append("Erro ao criar projeto: " + e1.getMessage() + newline);
                        log.invalidate();
                        return;
                    }
         }else if(e.getSource() == proximaVersaoButton)
            {
                   try
                    {

                       Versioning revision = new Versioning();
                       int nextRevision = revision.nextRevision(projetoField.getText());

                       if (projetoField.getText().isEmpty())
                        {
                            JOptionPane.showMessageDialog(null, "Campos de projeto ou consulta por versão podem estar vazios!", "Protótipo do Sistema Gardener", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "A proxima versão do projeto: "
                                    + projetoField.getText() +
                                    " é " + nextRevision,
                                    "Protótipo do Sistema Gardener", JOptionPane.INFORMATION_MESSAGE);
                        }

                       log.setText("");
                       projetoField.setText("");
                       consultaField.setText("");

                   }
                  catch(Exception e1)
                    {
                        log.append("Erro ao criar projeto: " + e1.getMessage() + newline);
                        log.invalidate();
                        return;
                    }
         }
	}

	private static void createAndShowGUI()
	{                            

                JFrame frame = new JFrame("Protótipo do Sistema Gardener");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JComponent newContentPane = new Principal();
		newContentPane.setOpaque(true);
		frame.setContentPane(newContentPane);

		//Mostrando a janela.
		frame.pack();
		frame.setVisible(true);
	}

        public static void main(String[] args)
                //throws TException, TimedOutException, InvalidRequestException, UnavailableException, UnsupportedEncodingException, NotFoundException, NullPointerException
        {
            javax.swing.SwingUtilities.invokeLater(new Runnable()
		{public void run() {
			createAndShowGUI();
			}
		});
	}
}


