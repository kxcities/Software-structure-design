package com.amazonaws.samples;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.util.List;
import java.util.Map.Entry;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.DeleteQueueRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;

public class Windows {	
	private JFrame frm;	
	private JTextField jtf = new JTextField(30);    // 创建文本行组件, 30 列
	private JTextField jtf1 = new JTextField(30);   // 创建文本行组件, 30 列
	private JTextArea jta = new JTextArea("操作提示：", 10, 30);           // 创建文本区组件,10行，30列
	private JScrollPane jsp = new JScrollPane(jta);                    // 创建滚动窗格，其显示内容是文本区对象
	private JButton btn = new JButton("建立队列");	
	private JButton btn1 = new JButton("发送消息");
	private JButton btn2 = new JButton("删除队列");
	private JButton btn3 = new JButton("保存操作记录");
	private JFrame frm1;
	private JTextArea jta1 = new JTextArea("操作提示：", 10, 30);           // 创建文本区组件,10行，30列
	private JScrollPane jsp1 = new JScrollPane(jta);                    // 创建滚动窗格，其显示内容是文本区对象
	private JButton btn4 = new JButton("接收消息");	
	private JButton btn5 = new JButton("删除消息");
	private JButton btn6 = new JButton("保存操作记录");
	public  Windows(){	
	    frm = new JFrame();
		frm.setBounds(400, 200,400, 400);   //设置窗口位于桌面的位置和大小
		frm.setTitle("消息队列操作界面");		
		Container c = frm.getContentPane(); //frm中包含一个内容窗格， 需要获取内容窗格，再设置背景颜色，直接设置frm的背景颜色会被内容窗格挡住		
		c.setBackground(Color.LIGHT_GRAY);
		jta.setLineWrap(true);           // 设置自动换行
		frm.setLayout(null);                //如过不设置为null默认，按钮会充满整个内容框，挡住背景颜色		
		frm.setVisible(true);	
		init();
	    frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    frm1 = new JFrame();
		frm1.setBounds(800,200,400,400);   //设置窗口位于桌面的位置和大小
		frm1.setTitle("消息接收操作界面");		
		Container c1 = frm1.getContentPane(); //frm中包含一个内容窗格， 需要获取内容窗格，再设置背景颜色，直接设置frm的背景颜色会被内容窗格挡住		
		c1.setBackground(Color.LIGHT_GRAY);
		jta1.setLineWrap(true);           // 设置自动换行
		frm1.setLayout(null);                //如过不设置为null默认，按钮会充满整个内容框，挡住背景颜色		
		frm1.setVisible(true);	
		init1();
	    frm1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);}
		
	private void init(){  	 
		//设置布局管理器       
		frm.setLayout(new FlowLayout());//设置为流式布局管理器
		//添加组件        
		frm.add(btn);
		frm.add(btn1);
		frm.add(btn2);
		frm.add(btn3);
        frm.add(jtf);
        frm.add(jtf1);
        frm.add(jta);
        frm.add(jsp); 
        /*
         * The ProfileCredentialsProvider will return your [default]
         * credential profile by reading from the credentials file located at
         * (C:\\Users\\kxmd3\\.aws\\credentials).
         */
        ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
        try {
            credentialsProvider.getCredentials();
        } catch (Exception e1) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (C:\\Users\\kxmd3\\.aws\\credentials), and is in valid format.",
                    e1);
        }

        AmazonSQS sqs = AmazonSQSClientBuilder.standard()
                               .withCredentials(credentialsProvider)
                               .withRegion(Regions.US_WEST_2)
                               .build();

        System.out.println("===========================================");
        System.out.println("Getting Started with Amazon SQS");
        System.out.println("===========================================\n");
        //创建事件处理        
        //btn事件源 
        btn.addActionListener(new ActionListener() {		
			public void actionPerformed(ActionEvent e) {
		        try {
		            // Create a queue
		        	jta.setText("Creating a new SQS queue.\n");
		        	String p=jtf.getText();
		            CreateQueueRequest createQueueRequest = new CreateQueueRequest(p);
		            String myQueueUrl = sqs.createQueue(createQueueRequest).getQueueUrl();
		            // List queues
		            System.out.println("Listing all queues in your account.\n");
		            for (String queueUrl : sqs.listQueues().getQueueUrls()) {
		                System.out.println("  QueueUrl: " + queueUrl);
		            }
		            System.out.println();
		        }catch (AmazonClientException ace) {
		            System.out.println("Caught an AmazonClientException, which means the client encountered " +
		                    "a serious internal problem while trying to communicate with SQS, such as not " +
		                    "being able to access the network.");
		            System.out.println("Error Message: " + ace.getMessage());
		        }
			}
        });
        btn1.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) {
            	try {
            		// Send a message
            		jta.setText("Sending a message to MyQueue.\n");
            		String p=jtf.getText();
                    String p1=jtf1.getText();
		            CreateQueueRequest createQueueRequest = new CreateQueueRequest(p);
		            String myQueueUrl = sqs.createQueue(createQueueRequest).getQueueUrl();
                    sqs.sendMessage(new SendMessageRequest(myQueueUrl, p1));
		        } catch (AmazonServiceException ase) {
		            System.out.println("Caught an AmazonServiceException, which means your request made it " +
		                    "to Amazon SQS, but was rejected with an error response for some reason.");
		            System.out.println("Error Message:    " + ase.getMessage());
		            System.out.println("HTTP Status Code: " + ase.getStatusCode());
		            System.out.println("AWS Error Code:   " + ase.getErrorCode());
		            System.out.println("Error Type:       " + ase.getErrorType());
		            System.out.println("Request ID:       " + ase.getRequestId());
		        } catch (AmazonClientException ace) {
		            System.out.println("Caught an AmazonClientException, which means the client encountered " +
		                    "a serious internal problem while trying to communicate with SQS, such as not " +
		                    "being able to access the network.");
		            System.out.println("Error Message: " + ace.getMessage());
		        }
            }
        });
        btn2.addActionListener(new ActionListener() {		
    		public void actionPerformed(ActionEvent e) {
                try {
                	// Delete a queue
                	jta.setText("Deleting the test queue.\n");
                	String p=jtf.getText();
                    CreateQueueRequest createQueueRequest = new CreateQueueRequest(p);
		            String myQueueUrl = sqs.createQueue(createQueueRequest).getQueueUrl();
                    sqs.deleteQueue(new DeleteQueueRequest(myQueueUrl));
		        }catch (AmazonClientException ace) {
		            System.out.println("Caught an AmazonClientException, which means the client encountered " +
		                    "a serious internal problem while trying to communicate with SQS, such as not " +
		                    "being able to access the network.");
		            System.out.println("Error Message: " + ace.getMessage());
		        }
    		    }
            });
        btn3.addActionListener(new ActionListener() {		
    		public void actionPerformed(ActionEvent e) {
    	    String str =jtf.getText();
            run(str);
            jta.setText("保存当前操作记录："+str+"  到文件log.txt之中");
    		    }
            });
	    }
	private void init1(){
		//设置布局管理器       
  		frm1.setLayout(new FlowLayout());//设置为流式布局管理器
  		//添加组件        
  		frm1.add(btn4);
  		//frm1.add(btn5);
  		frm1.add(btn6);
	    frm1.add(jta1);
	    frm1.add(jsp1);
	    /*
         * The ProfileCredentialsProvider will return your [default]
         * credential profile by reading from the credentials file located at
         * (C:\\Users\\kxmd3\\.aws\\credentials).
         */
        ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
        try {
            credentialsProvider.getCredentials();
        } catch (Exception e1) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (C:\\Users\\kxmd3\\.aws\\credentials), and is in valid format.",
                    e1);
        }

        AmazonSQS sqs = AmazonSQSClientBuilder.standard()
                               .withCredentials(credentialsProvider)
                               .withRegion(Regions.US_WEST_2)
                               .build();

        System.out.println("===========================================");
        System.out.println("Getting Started with Amazon SQS");
        System.out.println("===========================================\n");
        //创建事件处理        
        //btn事件源 
	    btn4.addActionListener(new ActionListener() {		
			public void actionPerformed(ActionEvent e) {
		        try {
		            System.out.println("Listing all queues in your account.\n");
		            for (String queueUrl : sqs.listQueues().getQueueUrls()) {
		                System.out.println("  QueueUrl: " + queueUrl);
		                // Receive messages
	                    System.out.println("Receiving messages from MyQueue.\n");
	                    ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl);
	                    List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
	                    for (Message message : messages) {
	                        System.out.println("  Message");
	                        System.out.println("    MessageId:     " + message.getMessageId());
	                        System.out.println("    ReceiptHandle: " + message.getReceiptHandle());
	                        System.out.println("    MD5OfBody:     " + message.getMD5OfBody());
	                        jta1.setText("    Body:          " + message.getBody());
	                        for (Entry<String, String> entry : message.getAttributes().entrySet()) {
	                            System.out.println("  Attribute");
	                            System.out.println("    Name:  " + entry.getKey());
	                            System.out.println("    Value: " + entry.getValue());
	                        }
	                    }
		            }
		            System.out.println();
		        }catch (AmazonClientException ace) {
		            System.out.println("Caught an AmazonClientException, which means the client encountered " +
		                    "a serious internal problem while trying to communicate with SQS, such as not " +
		                    "being able to access the network.");
		            System.out.println("Error Message: " + ace.getMessage());
		        }
			}
        });
	    /*
	    btn5.addActionListener(new ActionListener() {		
    		public void actionPerformed(ActionEvent e) {
    			try {
    			// Delete a message
    	        System.out.println("Deleting a message.\n");
    	        for (String queueUrl : sqs.listQueues().getQueueUrls()) {
	                System.out.println("  QueueUrl: " + queueUrl);
                ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl);
                List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
    	        String messageReceiptHandle = messages.get(0).getReceiptHandle();
    	        sqs.deleteMessage(new DeleteMessageRequest(queueUrl, messageReceiptHandle));
    		    }
    		}catch (AmazonClientException ace) {
	            System.out.println("Caught an AmazonClientException, which means the client encountered " +
	                    "a serious internal problem while trying to communicate with SQS, such as not " +
	                    "being able to access the network.");
	            System.out.println("Error Message: " + ace.getMessage());
    		});
	    */
        btn6.addActionListener(new ActionListener() {		
    		public void actionPerformed(ActionEvent e) {
    	    String str =jtf.getText();
            run(str);
            jta.setText("保存当前操作记录："+str+"  到文件log.txt之中");
    		    }
            });
	}
    public void run(String str){
    String rtnFile1 = "d:/log.txt";//输出文件路径
    try {
    BufferedWriter bw = new BufferedWriter(new FileWriter(rtnFile1));
    String str1 = str ;
    bw.write(str1);
    bw.newLine();
    bw.flush();
    bw.close();
    } catch (IOException e) {
    e.printStackTrace();
    }
}
	public static void main(String[] args) {		
		new Windows();
	}
}