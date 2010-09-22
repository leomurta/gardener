/*
 * Autor: Evaldo de Oliveira da Silva
 * Descrição: Protótipo para modelo de versionamento do sistema Gardener
 */
package versionamento;

import java.io.*;
import java.io.FileInputStream.*;
import java.io.DataInputStream.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.io.UnsupportedEncodingException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.mongodb.Mongo;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;
import com.mongodb.gridfs.GridFS;


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
          , destinoButton;
    
    JTextField comentarioField
             , usuarioField
             , dataField
             , consultaField
             , origemField
             , destinoField;

    JLabel comentarioLabel
         , usuarioLabel
         , dataLabel
         , consultaLabel;

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

	openButton = new JButton("Checkout"); //Implementar imagem.
	openButton.addActionListener(this);
	
	sendButton = new JButton("Checkin");
	sendButton.addActionListener(this);
	
	cancelButton = new JButton("Cancelar");
	cancelButton.addActionListener(this);

        consultaButton = new JButton("Consultar");
        consultaButton.addActionListener(this);

        origemButton = new JButton("Diretorio Origem");
	origemButton.addActionListener(this);

        destinoButton = new JButton("Diretorio Destino");
	destinoButton.addActionListener(this);
	       
        comentarioLabel = new JLabel("Log: ");
        usuarioLabel = new JLabel("Usuario: ");
        dataLabel = new JLabel("Data: ");
        consultaLabel = new JLabel("Consulta por revisao: ");

        comentarioField = new JTextField(30);
        usuarioField = new JTextField(25);
        dataField = new JTextField(10);
        consultaField = new JTextField(3);
        origemField = new JTextField(10);
        destinoField = new JTextField(10);         

        JPanel camposPanel = new JPanel();
        camposPanel.add(consultaLabel);
        camposPanel.add(consultaField);
        camposPanel.add(comentarioLabel);
        camposPanel.add(comentarioField);
        camposPanel.add(usuarioLabel);
        camposPanel.add(usuarioField);
        camposPanel.add(dataLabel);
        camposPanel.add(dataField);        

	JPanel buttonPanel = new JPanel();
        buttonPanel.add(sendButton);
        buttonPanel.add(consultaButton);
	buttonPanel.add(cancelButton);       

	//Adicionando os recursos no painel.                
        add(logScrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.PAGE_END);
        add(camposPanel, BorderLayout.PAGE_START);        
    }

    public void actionPerformed(ActionEvent e)
    {
	//Acão nos botões.
	if(e.getSource() == openButton)
	{

           FileInputStream arqOrigem;
           FileInputStream arqDestino;

	   try
		{

                    Mongo connect = new Mongo( "localhost" , 27017 );
                    DB conn = connect.getDB( "gardener" );
                    GridFS arqRevisao = new GridFS(conn);

                    arqOrigem = new FileInputStream(arqRevisao.findOne(consultaField.getText()).getFilename());
                    arqDestino = new FileInputStream("c:/"+consultaField.getText());

                    fcOrigem  = arqOrigem.getChannel();
                    fcDestino = arqDestino.getChannel();

                    fcOrigem.transferTo(0, fcOrigem.size(), fcDestino);

                    log.append("Realizando chekout do arquivo:" + origemField.getText() +  newline);
		}
		catch(Exception e1)
        	{
                    log.append("Erro ao realizar o checkout do arquivo: " + e1.getMessage() + newline);
                    log.invalidate();
                    return;
        	}

        } else if(e.getSource() == sendButton)
            {
                int returnVal = fc.showOpenDialog(Principal.this);

                if(returnVal == JFileChooser.APPROVE_OPTION)
                {
                    File file = fc.getSelectedFile();
                    long fileSize = file.length();

                    //streams
                    FileInputStream arqOrigem;
                    FileOutputStream arqDestino;                                       

                    try
                    {
                        log.append("Realizando ckeckin do arquivo:" + file.getName() +  newline);
                        log.append("Tamanho do arquivo: " + fileSize + "bytes" + newline);                       

                        Mongo connect = new Mongo( "localhost" , 27017 );
                        DB conn = connect.getDB( "gardener" );
                        DBCollection collection = conn.getCollection("gardener");
                        
                        FileReader fr = new FileReader("c:/projeto_gardener/arquivo_controle.conf");              
                        BufferedReader buff = new BufferedReader(fr);

                        int valorRevisao = Integer.parseInt(buff.readLine());
                        fr.close();

                        valorRevisao = valorRevisao + 1;

                        FileWriter fw = new FileWriter("c:/projeto_gardener/arquivo_controle.conf", false);
                        fw.write(Integer.toString(valorRevisao));
                        fw.close();
                        
                        String valorRevisaoS = Integer.toString(valorRevisao);

                        File dir = new File("c:/projeto_gardener" + "/" + valorRevisao);
                        if (dir.mkdir()) {
                            log.append("Diretorio criado com sucesso!" + newline);
                        } else {
                            log.append("Erro ao criar diretorio!" + newline);
                        }                       

                        arqOrigem = new FileInputStream(file.getAbsolutePath());
                        arqDestino = new FileOutputStream("c:/projeto_gardener/"+ valorRevisao + "/" + file.getName());
                        
                        fcOrigem  = arqOrigem.getChannel();
                        fcDestino = arqDestino.getChannel();

                        log.append("A revisão anterior deste arquivo é: " + (valorRevisao-1) + " . A próxima revisão será: " + valorRevisao+ newline);

                        GridFS arqRevisao = new GridFS(conn);
                        arqRevisao.createFile(arqOrigem, valorRevisaoS).save();

                        BasicDBObject revisao = new BasicDBObject();
                        revisao.put("revisao", valorRevisaoS);
                        revisao.put("data", dataField.getText().toString());
                        revisao.put("comentario", comentarioField.getText().toString());
                        revisao.put("usuario", usuarioField.getText().toString());                        

                        collection.insert(revisao);
                        
                        fcOrigem.transferTo(0, fcOrigem.size(), fcDestino);

                        comentarioField.setText("");
                        usuarioField.setText("");
                        dataField.setText("");

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

                        Mongo connect = new Mongo( "localhost" , 27017 );
                        DB conn = connect.getDB( "gardener" );
                        DBCollection collection = conn.getCollection("gardener");

                        log.setText("");

                        BasicDBObject revisao = new BasicDBObject();
                        revisao.put("revisao", consultaField.getText());

                        DBCursor resultadoCons = collection.find();
                        resultadoCons = collection.find(revisao);

                        while(resultadoCons.hasNext()) {
                            log.append(resultadoCons.next() + "" + newline);
                        }
                        
                         GridFS arqRevisao = new GridFS(conn);
                         log.append(arqRevisao.findOne(consultaField.getText()).toString());
                         

                   }
                  catch(Exception e1)
                    {
                        log.append("Erro ao realizar a consulta: " + e1.getMessage() + newline);
                        log.invalidate();
                        return;
                    }
            }
	}

	private static void createAndShowGUI()
	{                            

                JFrame frame = new JFrame("Protótipo da API Versionamento do Sistema Gardener");
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


