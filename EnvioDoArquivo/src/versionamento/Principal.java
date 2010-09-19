package versionamento;

import java.io.*;
import java.io.FileInputStream.*;
import java.io.DataInputStream.*;
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

import org.apache.cassandra.thrift.*;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.ColumnPath;
import org.apache.cassandra.thrift.Cassandra;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.TException;
import org.apache.cassandra.thrift.ConsistencyLevel;

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

        comentarioField = new JTextField(50);
        usuarioField = new JTextField(25);
        dataField = new JTextField(10);
        consultaField = new JTextField(5);
        origemField = new JTextField(10);
        destinoField = new JTextField(10);

        //Paineis
        JPanel consultaPanel = new JPanel();
        consultaPanel.add(consultaLabel);
        consultaPanel.add(consultaField);
        consultaPanel.add(consultaButton);       

        JPanel camposPanel = new JPanel();
        camposPanel.add(comentarioLabel);
        camposPanel.add(comentarioField);
        camposPanel.add(usuarioLabel);
        camposPanel.add(usuarioField);
        camposPanel.add(dataLabel);
        camposPanel.add(dataField);

	JPanel buttonPanel = new JPanel();
        buttonPanel.add(sendButton);
        buttonPanel.add(openButton);
	buttonPanel.add(cancelButton);
        buttonPanel.add(origemButton);
        buttonPanel.add(destinoButton);

	//Adicionando os recursos no painel.
        add(logScrollPane, BorderLayout.CENTER);
        add(consultaPanel, BorderLayout.BEFORE_LINE_BEGINS);
        add(buttonPanel, BorderLayout.PAGE_END);
        add(camposPanel, BorderLayout.PAGE_START);
    }

    public void actionPerformed(ActionEvent e)
    {
	//Acão nos botões.
	if(e.getSource() == openButton)
	{

           FileInputStream arqOrigem;
           FileOutputStream arqDestino;

	   try
		{

                  arqOrigem = new FileInputStream(origemField.getText());
                  arqDestino = new FileOutputStream(destinoField.getText());

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

                        TTransport tr = new TSocket("localhost", 9160);
                        TProtocol proto = new TBinaryProtocol(tr);
                        Cassandra.Client client = new Cassandra.Client(proto);
                        tr.open();

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

                        ColumnPath pathRevisao = new ColumnPath().setColumn_family("versao").setColumn("revisao".getBytes());

                        long timestamp = System.currentTimeMillis();

                        client.insert("gardener",
                                        valorRevisaoS.toString(),
                                        pathRevisao,
                                        valorRevisaoS.toString().getBytes("UTF-8"),
                                        timestamp,
                                        ConsistencyLevel.ONE);
                        
                        tr.close();

                        tr.open();

                        String arquivoVersao = "c:/projeto/gardener/"+ valorRevisaoS + "/" + file.getName().toString();

                        ColumnPath pathDocumento = new ColumnPath().setColumn_family("versao").setColumn("documento".getBytes());

                        client.insert("gardener",
                                        valorRevisaoS.toString(),
                                        pathDocumento,
                                        arquivoVersao.getBytes("UTF-8"),
                                        timestamp,
                                        ConsistencyLevel.ONE);

                        tr.close();

                        tr.open();
                        ColumnPath pathData = new ColumnPath().setColumn_family("versao").setColumn("data".getBytes());
                        
                        client.insert("gardener",
                                        valorRevisaoS.toString(),
                                        pathData,
                                        dataField.getText().toString().getBytes("UTF-8"),
                                        timestamp,
                                        ConsistencyLevel.ONE);

                        tr.close();

                        tr.open();

                        ColumnPath pathComentario = new ColumnPath().setColumn_family("versao").setColumn("comentario".getBytes());
                        
                        client.insert("gardener",
                                        valorRevisaoS.toString(),
                                        pathComentario,
                                        comentarioField.getText().toString().getBytes("UTF-8"),
                                        timestamp,
                                        ConsistencyLevel.ONE);

                        tr.close();

                        tr.open();

                        ColumnPath pathUsuario = new ColumnPath().setColumn_family("versao").setColumn("usuario".getBytes());

                        client.insert("gardener",
                                        valorRevisaoS.toString(),
                                        pathUsuario,
                                        usuarioField.getText().toString().getBytes("UTF-8"),
                                        timestamp,
                                        ConsistencyLevel.ONE);

                        tr.close();
                        
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

                        TTransport tr = new TSocket("localhost", 9160);
                        TProtocol proto = new TBinaryProtocol(tr);
                        Cassandra.Client client = new Cassandra.Client(proto);
                        tr.open();

                        log.setText("");

                        ColumnPath comentario = new ColumnPath().setColumn_family("versao").setColumn("comentario".getBytes());
                        ColumnOrSuperColumn colComentario = client.get("gardener", consultaField.getText().toString(), comentario,  ConsistencyLevel.ONE);
                        
                        log.append(new String(colComentario.getColumn().getName(), "UTF-8") + " -> " + new String(colComentario.getColumn().getValue(), "UTF-8") + newline);

                        ColumnPath data = new ColumnPath().setColumn_family("versao").setColumn("data".getBytes());
                        ColumnOrSuperColumn colData = client.get("gardener", consultaField.getText().toString(), data,  ConsistencyLevel.ONE);

                        log.append(new String(colData.getColumn().getName(), "UTF-8") + " -> " + new String(colData.getColumn().getValue(), "UTF-8") + newline);

                        ColumnPath usuario = new ColumnPath().setColumn_family("versao").setColumn("usuario".getBytes());
                        ColumnOrSuperColumn colUsuario = client.get("gardener", consultaField.getText().toString(), usuario,  ConsistencyLevel.ONE);

                        log.append(new String(colUsuario.getColumn().getName(), "UTF-8") + " -> " + new String(colUsuario.getColumn().getValue(), "UTF-8") + newline);

                        tr.close();


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
                throws TException, TimedOutException, InvalidRequestException, UnavailableException, UnsupportedEncodingException, NotFoundException, NullPointerException
        {
            javax.swing.SwingUtilities.invokeLater(new Runnable()
		{public void run() {
			createAndShowGUI();
			}
		});
	}
}


