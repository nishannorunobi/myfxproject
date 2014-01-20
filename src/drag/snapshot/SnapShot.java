package drag.snapshot;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Random;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


public class SnapShot extends JFrame {
	  static ImageArea ia = new ImageArea();
          static String imageFormat = "jpeg";

   // private static void createPngImageFolder(String directoryURL, long fileNameSuffix) {
    private static void createPngImageFolder(String directoryURL, String fileNameSuffix) {
         ImageWriter writer = null;
	    ImageOutputStream ios = null;
            String imageFormat="png";
	    try {
	      Iterator iter = ImageIO.getImageWritersByFormatName(imageFormat);

	      if (!iter.hasNext()) {
	        System.out.println("Unable to save image to png file type.");
	        return;
	      }
	      writer = (ImageWriter) iter.next();
	      String directoryPath = directoryURL+"mobileSupportedImage/";
	      File directory = new File(directoryPath);
	      if(!directory.exists()){
	    	 boolean result =  directory.mkdir();
	    	 if(!result){
	    		 JOptionPane.showMessageDialog (null, "Directory could not created\n in order to save the image", "Title", JOptionPane.WARNING_MESSAGE);
	    	 }
	      }
             
              String fullImagePath = directoryPath+"image"+fileNameSuffix+"."+imageFormat;
              File imageFile = new File(fullImagePath);
              ios = ImageIO.createImageOutputStream(imageFile);
	      writer.setOutput(ios);
	      ImageWriteParam iwp = writer.getDefaultWriteParam();
	     // iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
	    // iwp.setCompressionQuality(0.95f);
	      writer.write(null,
	          new IIOImage((BufferedImage) ia.getImage(), null, null), iwp);
	    } catch (Exception e2) {
	      e2.printStackTrace();
	    }
    }

    private static String getNowDateTime() {
        String now = new SimpleDateFormat("yyyyMMddhhmmss").format(new java.util.Date());
        return now;
    }

     
	  Rectangle rectScreenSize = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());

	  static Robot robot;

	  static JScrollPane jsp;

	  public SnapShot(String title) {
	    super(title);
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	    getContentPane().add(jsp = new JScrollPane(ia));
	    setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
	    pack();
	    setVisible(true);
	    
	    //////////////////
	    
	    BufferedImage biScreen = robot.createScreenCapture(rectScreenSize);
        setVisible(true);

        ia.setImage(biScreen);

        jsp.getHorizontalScrollBar().setValue(0);
        jsp.getVerticalScrollBar().setValue(0);
	    
	    
	    ///////////////////////////
            
	  }
          /////////////////
           public static long countFilesInDirectory(File dir) {  
                int totalFiles = 0;
                File[] listFiles = dir.listFiles();  
                if (listFiles != null && listFiles.length > 0) {  
                    for (File file : listFiles) {  
                        if (file.isFile()) {  
                            totalFiles++;  
                        } else {  
                            totalFiles += countFilesInDirectory(file);  
                        }  
                    }  
                }  
                return totalFiles;  
            }
          //////////////////
          public static long getRandomName()throws Exception {  
            Random generator = new Random();  
            generator.setSeed(System.currentTimeMillis());  

            long num = generator.nextInt(99999) + 99999;  
            if (num < 100000 || num > 999999) {  
            num = generator.nextInt(99999) + 99999;  
            if (num < 100000 || num > 999999) {  
            throw new Exception("Unable to generate PIN at this time..");  
            }  
            }  
            return num;
          }
           
           
           //////////////////
	  public static void save() {
	    if (ia.getImage() == null) {
	      System.out.println("No captured image.");
	      return;
	    }
	    ImageWriter writer = null;
	    ImageOutputStream ios = null;

	    try {
	      Iterator iter = ImageIO.getImageWritersByFormatName(imageFormat);

	      if (!iter.hasNext()) {
	        System.out.println("Unable to save image to jpeg file type.");
	        return;
	      }
	      writer = (ImageWriter) iter.next();
	      String directoryPath = "C:/nishanSnapShot/";
	      File directory = new File(directoryPath);
	      if(!directory.exists()){
	    	 boolean result =  directory.mkdir();
	    	 if(!result){
	    		 JOptionPane.showMessageDialog (null, "Directory could not created\n in order to save the image", "Title", JOptionPane.WARNING_MESSAGE);
	    	 }
	      }
              //long fileNameSuffix = countFilesInDirectory(directory);
	      String fileNameSuffix = getNowDateTime();
              String fullImagePath = directoryPath+"image"+fileNameSuffix+"."+imageFormat;
              File imageFile = new File(fullImagePath);
              while(true){
                if(imageFile.exists()){
                    fileNameSuffix = getRandomName()+"";
                    fullImagePath = directoryPath+"image"+fileNameSuffix+"."+imageFormat;
                    imageFile = new File(fullImagePath);
                }else{
                    break;
                }
              }
              ios = ImageIO.createImageOutputStream(imageFile);
	      writer.setOutput(ios);
	      ImageWriteParam iwp = writer.getDefaultWriteParam();
	      iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
	      iwp.setCompressionQuality(0.95f);
	      writer.write(null,new IIOImage((BufferedImage) ia.getImage(), null, null), iwp);
              
              createPngImageFolder(directoryPath, fileNameSuffix);
	    } catch (Exception e2) {
	      e2.printStackTrace();
	    }
	  }

	  public static void main(String[] args) throws Exception {
	    robot = new Robot();
	    new SnapShot("Capture");
	  }
	}
	class ImageArea extends JPanel {
	  private Image image;

	  Point startPoint = new Point(), endPoint = new Point();

	  private Rectangle selectedRectangle = new Rectangle();

	  public ImageArea() {
              ////////////////////add key press listener
              KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
            manager.addKeyEventDispatcher(new KeyEventDispatcher() {

                  @Override
                  public boolean dispatchKeyEvent(KeyEvent e) {
                       if(e.getKeyCode()==KeyEvent.VK_ESCAPE){
                           System.exit(0);
                           return true;
                       }
                      return false;
                  }
              });
              /////////////////////
	   addMouseListener(new MouseAdapter() {
	      public void mousePressed(MouseEvent e) {
	        if (image == null)
	          return;
	        startPoint = endPoint = e.getPoint();
	        repaint();
	      }
	    });

	   addMouseMotionListener(new MouseMotionAdapter() {
	      public void mouseDragged(MouseEvent e) {
	        if (image == null)
	          return;
	        endPoint = e.getPoint();
	        repaint();
	      }
	    });
	    //////////////
	    addMouseListener(new MouseListener() {
			
	    	@Override
			public void mousePressed(MouseEvent e) {
			}
	    	
			@Override
			public void mouseReleased(MouseEvent e) {
				if (crop()) {
			          SnapShot.jsp.getHorizontalScrollBar().setValue(0);
			          SnapShot.jsp.getVerticalScrollBar().setValue(0);
			          SnapShot.save();
			          int status=0;
			       
			         System.exit(status);
			          
			     }
				
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	  }

	  public boolean crop() {
	    if (startPoint.equals(endPoint))
	      return true;

	    boolean succeeded = true;

	    int x1 = (startPoint.x < endPoint.x) ? startPoint.x : endPoint.x;
	    int y1 = (startPoint.y < endPoint.y) ? startPoint.y : endPoint.y;

	    int x2 = (startPoint.x > endPoint.x) ? startPoint.x : endPoint.x;
	    int y2 = (startPoint.y > endPoint.y) ? startPoint.y : endPoint.y;

	    int width = (x2 - x1) + 1;
	    int height = (y2 - y1) + 1;

	    BufferedImage biCrop = new BufferedImage(width, height,
	        BufferedImage.TYPE_INT_RGB);
	    Graphics2D g2d = biCrop.createGraphics();
	    BufferedImage bi = (BufferedImage) image;
	    BufferedImage bi2 = bi.getSubimage(x1, y1, width, height);
	    g2d.drawImage(bi2, null, 0, 0);

	    g2d.dispose();

	    if (succeeded)
	      setImage(biCrop);
	    else {
	      startPoint.x = endPoint.x;
	      startPoint.y = endPoint.y;
	      repaint();
	    }

	    return succeeded;
	  }

	  public Image getImage() {
	    return image;
	  }

	  public void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    if (image != null)
	      g.drawImage(image, 0, 0, this);

	    if (startPoint.x != endPoint.x || startPoint.y != endPoint.y) {
	      int x1 = (startPoint.x < endPoint.x) ? startPoint.x : endPoint.x;
	      int y1 = (startPoint.y < endPoint.y) ? startPoint.y : endPoint.y;
	      int x2 = (startPoint.x > endPoint.x) ? startPoint.x : endPoint.x;
	      int y2 = (startPoint.y > endPoint.y) ? startPoint.y : endPoint.y;
	      selectedRectangle.x = x1;
	      selectedRectangle.y = y1;
	      selectedRectangle.width = (x2 - x1) + 1;
	      selectedRectangle.height = (y2 - y1) + 1;
	      Graphics2D g2d = (Graphics2D) g;
	      g2d.draw(selectedRectangle);
	    }
	  }

	  public void setImage(Image image) {
	    this.image = image;
	    setPreferredSize(new Dimension(image.getWidth(this), image.getHeight(this)));
	    revalidate();
	    startPoint.x = endPoint.x;
	    startPoint.y = endPoint.y;
	    repaint();
	  }

}
