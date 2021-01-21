package com.retailx.dreamdx.retailx.utils;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.retailx.dreamdx.retailx.POJO.Invoice;
import com.retailx.dreamdx.retailx.POJO.Product;
import com.retailx.dreamdx.retailx.POJO.ProductDetailsListGrid;
import com.retailx.dreamdx.retailx.database.DBHelper;
import com.retailx.dreamdx.retailx.sharedpreference.Constants;
import com.retailx.dreamdx.retailx.sharedpreference.SharedPreference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class PdfGenerator {


    static boolean mExternalStorageAvailable = false;
    static boolean mExternalStorageWriteable = false;
    private void checkExternalStorageAvailable(){

        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Something else is wrong. It may be one of many other states, but all we need
            //  to know is we can neither read nor write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }
    }

    static String path="";
    static String absolutePath="";
    static File dir=null;
    static ArrayList<ProductDetailsListGrid> selectedList=null;


    /* Method for creating a pdf file from text, saving it then opening it for display*/
    public static void  createAndDisplayPdf(Context ctx) {
        selectedList=Product.getSelectedListDupliacteRemoved();
        Document doc = new Document();

        try {

            if(mExternalStorageAvailable && mExternalStorageWriteable) {

                path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Dir";
            }else{
                //path=getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath()+ "/Dir";
                //Validator.showToast(this,"sd card not available, Cannot create pdf");
                if (Build.VERSION.SDK_INT >= 19) {
                    path = ctx.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath()+ "/Dir";
                }else{
                    path = Environment.getExternalStorageDirectory() + "/Documents"+ "/Dir";
                }

            }

            dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();

            File file = new File(dir, "invoice.pdf");
            absolutePath=file.getAbsolutePath();
            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter.getInstance(doc, fOut);

            //open the document
            doc.open();

            addHeader(doc,ctx);

            // LINE SEPARATOR
            doc.add(new Paragraph(""));
            doc.add(new Paragraph(""));
            doc.add(new Paragraph("\n"));
            doc.add(new Paragraph("\n"));

            Font font = new Font();
            font.setColor(new BaseColor(234, 66, 39));
            font.setSize(15f);

           /* Phrase titlePh = new Phrase("RETAILX",font);
            Paragraph title = new Paragraph(titlePh);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            doc.add(title);*/

            font.setSize(15f);
            font.setColor(new BaseColor(125,125,126));
            Paragraph p1 = new Paragraph("#Receipt "+Invoice.getInstance().getTransactionSummaryObj().getTransactionId(),font);
            p1.setAlignment(Paragraph.ALIGN_RIGHT);
            doc.add(p1);

            try {
                if (Invoice.getInstance().getBussinessInfoObj() != null) {

                    doc.add(new Paragraph(""));
                    doc.add(new Paragraph(""));
                    doc.add(new Paragraph("\n"));
                    doc.add(new Paragraph("\n"));

                    PdfPTable table = new PdfPTable(2);
                    table.setWidthPercentage(95);
                    table.setWidths(new int[]{4, 1});

                    PdfPCell cell = new PdfPCell();

                    cell = new PdfPCell(createTableCell(Invoice.getInstance().getBussinessInfoObj().getBussinessName(),
                            Invoice.getInstance().getBussinessInfoObj().getBussinessAddress(), Invoice.getInstance().getBussinessInfoObj().getBussinesPhoneNo()));
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);

                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(createImageCell(Invoice.getInstance().getBussinessInfoObj().getLogoPath()));


                    doc.add(table);

                    font.setSize(25f);
                    font.setColor(new BaseColor(234, 66, 39));

                    doc.add(new Paragraph(""));
                    doc.add(new Paragraph(""));
                    doc.add(new Paragraph("\n"));
                    doc.add(new Paragraph("\n"));

                }
            }catch (Exception e){

            }

            // LINE SEPARATOR
            LineSeparator lineSeparator = new LineSeparator();
            lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));

            doc.add(new Paragraph(""));
            doc.add(new Chunk(lineSeparator));
            doc.add(new Paragraph(""));


            doc.add(new Paragraph("\n"));


            PdfPTable tableheader = new PdfPTable(4);
            tableheader.setWidthPercentage(95);
            tableheader.setWidths(new int[]{1, 4,2,2});

           /* Phrase phrase = new Phrase();
            phrase.add("INDEX");
            Paragraph para = new Paragraph();
            para.add(phrase);
            para.setAlignment(Element.ALIGN_CENTER);

            PdfPCell cellHeader = new PdfPCell(phrase);
            cellHeader.setBorder(Rectangle.NO_BORDER);
            cellHeader.setVerticalAlignment(Element.ALIGN_CENTER);
            cellHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableheader.addCell(cellHeader);*/

            Phrase phrase = new Phrase();
            phrase.add("Qty");
            font.setSize(15f);
            font.setColor(new BaseColor(255,255,255));
            Paragraph para = new Paragraph("Qty",font);
            //para.add(phrase);
            para.setAlignment(Element.ALIGN_CENTER);
            PdfPCell cellHeader = new PdfPCell(new Phrase(para));
            //cellHeader.setBorder(Rectangle.NO_BORDER);
            cellHeader.setVerticalAlignment(Element.ALIGN_CENTER);
            cellHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellHeader.setBackgroundColor(BaseColor.BLACK);
            cellHeader.setPadding(5f);
            tableheader.addCell(cellHeader);


            phrase = new Phrase();
            phrase.add("Product");
            para = new Paragraph("Product",font);
            //para.add(phrase);
            para.setAlignment(Element.ALIGN_LEFT);
            cellHeader = new PdfPCell(new Phrase(para));
            //cellHeader.setBorder(Rectangle.NO_BORDER);
            cellHeader.setVerticalAlignment(Element.ALIGN_CENTER);
            cellHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellHeader.setBackgroundColor(BaseColor.BLACK);
            tableheader.addCell(cellHeader);


            phrase = new Phrase();
            phrase.add("Unit Price");
            para = new Paragraph("Price",font);
            //para.add(phrase);
            para.setAlignment(Element.ALIGN_LEFT);
            cellHeader = new PdfPCell(new Phrase(para));
            //cellHeader.setBorder(Rectangle.NO_BORDER);
            cellHeader.setVerticalAlignment(Element.ALIGN_CENTER);
            cellHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellHeader.setBackgroundColor(BaseColor.BLACK);
            tableheader.addCell(cellHeader);





            phrase = new Phrase();
            phrase.add("TOTAL");
            para = new Paragraph("Total",font);
            //para.add(phrase);
            para.setAlignment(Element.ALIGN_LEFT);
            cellHeader = new PdfPCell(new Phrase(para));
            //cellHeader.setBorder(Rectangle.NO_BORDER);
            cellHeader.setVerticalAlignment(Element.ALIGN_CENTER);
            cellHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellHeader.setBackgroundColor(BaseColor.BLACK);
            tableheader.addCell(cellHeader);



            //tableheader.setTotalWidth(PageSize.A4.getWidth());
            //tableheader.setLockedWidth(true);

            doc.add(tableheader);

           // doc.add(new Paragraph("\n"));

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(95);
            table.setWidths(new int[]{1, 4,2,2});
            font.setColor(new BaseColor(75,75,75));


            for(int i = 0; i < selectedList.size(); i++){

                font.setSize(15f);

                phrase = new Phrase();
                phrase.add(String.valueOf(selectedList.get(i).getItemCount()));

                if(selectedList.get(i).getUnitOfMeasure().equalsIgnoreCase("0")) {
                    para = new Paragraph(String.valueOf(selectedList.get(i).getItemCount()), font);
                }else{
                    para = new Paragraph(String.valueOf((int)selectedList.get(i).getItemCount()), font);

                }
                // para.add(phrase);
                para.setAlignment(Element.ALIGN_CENTER);
                PdfPCell cellOne = new PdfPCell(new Phrase(para));
                //cellOne.setBorder(Rectangle.NO_BORDER);
                cellOne.setVerticalAlignment(Element.ALIGN_CENTER);
                cellOne.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellOne.setPadding(10f);
                table.addCell(cellOne);

                phrase = new Phrase();
                phrase.add(selectedList.get(i).getTitle());
                para = new Paragraph(selectedList.get(i).getTitle(),font);
                //para.add(phrase);
                para.setAlignment(Element.ALIGN_LEFT);
                cellOne = new PdfPCell(new Phrase(para));
                //cellOne.setBorder(Rectangle.NO_BORDER);
                cellOne.setPadding(10f);
                cellOne.setVerticalAlignment(Element.ALIGN_CENTER);
                cellOne.setHorizontalAlignment(Element.ALIGN_CENTER);;
                table.addCell(cellOne);

                phrase = new Phrase();
                phrase.add(AppFeatures.format(selectedList.get(i).getUnitPrice()));
                para = new Paragraph(AppFeatures.format(selectedList.get(i).getUnitPrice()),font);
                //para.add(phrase);
                para.setAlignment(Element.ALIGN_LEFT);
                cellOne = new PdfPCell(new Phrase(para));
                cellOne.setPadding(10f);
                //cellOne.setBorder(Rectangle.NO_BORDER);
                cellOne.setVerticalAlignment(Element.ALIGN_CENTER);
                cellOne.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cellOne);



                phrase = new Phrase();
                phrase.add(AppFeatures.format(selectedList.get(i).getUnitPrice()*selectedList.get(i).getItemCount()));
                para = new Paragraph(AppFeatures.format(selectedList.get(i).getUnitPrice()*selectedList.get(i).getItemCount()),font);
                //para.add(phrase);
                para.setAlignment(Element.ALIGN_LEFT);
                cellOne = new PdfPCell(new Phrase(para));
                cellOne.setPadding(10f);
                //cellOne.setBorder(Rectangle.NO_BORDER);
                cellOne.setVerticalAlignment(Element.ALIGN_CENTER);
                cellOne.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cellOne);


            }


            //table.setTotalWidth(PageSize.A4.getWidth());
            //table.setLockedWidth(true);
            doc.add(table);


            doc.add(new Paragraph("\n"));

            lineSeparator = new LineSeparator();
            lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));


            doc.add(new Paragraph("\n"));


            //table summary
            PdfPTable tableSummary = new PdfPTable(2);
            tableSummary.setWidthPercentage(95);

            phrase = new Phrase();
            phrase.add("Sub Total");
            font.setColor(new BaseColor(112,112,112));
            font.setSize(15f);
            para = new Paragraph("Sub Total",font);
            // para.add(phrase);
            para.setAlignment(Element.ALIGN_CENTER);

            PdfPCell cellSummary = new PdfPCell(para);
            cellSummary.setBorder(Rectangle.NO_BORDER);
            cellSummary.setVerticalAlignment(Element.ALIGN_CENTER);
            cellSummary.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableSummary.addCell(cellSummary);


            phrase = new Phrase();
            font.setColor(new BaseColor(75,75,75));

            phrase.add(AppFeatures.format(Product.getSelectedProductTotal()));
            para = new Paragraph(AppFeatures.format(Product.getSelectedProductTotal()),font);
            //para.add(phrase);
            para.setAlignment(Element.ALIGN_CENTER);
            cellSummary = new PdfPCell(para);
            cellSummary.setBorder(Rectangle.NO_BORDER);
            cellSummary.setVerticalAlignment(Element.ALIGN_CENTER);
            cellSummary.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tableSummary.addCell(cellSummary);



            phrase = new Phrase();
            phrase.add("Discount");
            font.setColor(new BaseColor(112,112,112));

            para = new Paragraph("Discount",font);
            //para.add(phrase);
            para.setAlignment(Element.ALIGN_CENTER);

            cellSummary = new PdfPCell(para);
            cellSummary.setBorder(Rectangle.NO_BORDER);
            cellSummary.setVerticalAlignment(Element.ALIGN_CENTER);
            cellSummary.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableSummary.addCell(cellSummary);


            phrase = new Phrase();
            font.setColor(new BaseColor(75,75,75));
            phrase.add(AppFeatures.format(Product.getDiscount()));
            para = new Paragraph(AppFeatures.format(Product.getDiscount()),font);
            //para.add(phrase);
            para.setAlignment(Element.ALIGN_CENTER);
            cellSummary = new PdfPCell(para);
            cellSummary.setBorder(Rectangle.NO_BORDER);
            cellSummary.setVerticalAlignment(Element.ALIGN_CENTER);
            cellSummary.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tableSummary.addCell(cellSummary);


            phrase = new Phrase();
            phrase.add("Total");
            font.setColor(new BaseColor(112,112,112));

            para = new Paragraph("Total",font);
            // para.add(phrase);
            para.setAlignment(Element.ALIGN_CENTER);
            float fntSize, lineSpacing;
            fntSize = 20.0f;
            lineSpacing = 10f;
            para = new Paragraph(new Phrase(lineSpacing,"Total",
                    FontFactory.getFont(FontFactory.TIMES_BOLD, fntSize)));
            cellSummary = new PdfPCell(para);
            cellSummary.setBorder(Rectangle.NO_BORDER);
            cellSummary.setVerticalAlignment(Element.ALIGN_CENTER);
            cellSummary.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableSummary.addCell(cellSummary);


            phrase = new Phrase();
            font.setColor(new BaseColor(75,75,75));

            phrase.add(AppFeatures.format(Product.getSelectedProductTotal()-Product.getDiscount()));
            para = new Paragraph(AppFeatures.format(Product.getSelectedProductTotal()-Product.getDiscount()),font);
            //para.add(phrase);
            para = new Paragraph(new Phrase(lineSpacing,AppFeatures.format(Product.getSelectedProductTotal()-Product.getDiscount()),
                    FontFactory.getFont(FontFactory.TIMES_BOLD, fntSize)));
            para.setAlignment(Element.ALIGN_CENTER);
            cellSummary = new PdfPCell(para);
            cellSummary.setBorder(Rectangle.NO_BORDER);
            cellSummary.setVerticalAlignment(Element.ALIGN_CENTER);
            cellSummary.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tableSummary.addCell(cellSummary);


            doc.add(tableSummary);

            doc.add(new Paragraph("\n"));
            font.setSize(15f);
            p1 = new Paragraph("Thank You",font);
            p1.setAlignment(Paragraph.ALIGN_CENTER);
            doc.add(p1);

            doc.add(new Paragraph(""));
            doc.add(new Chunk(lineSeparator));
            doc.add(new Paragraph(""));

            doc.add(new Paragraph(""));
            doc.add(new Paragraph(""));
            doc.add(new Paragraph("\n"));
            doc.add(new Paragraph("\n"));
            
            /*font.setSize(15f);
            font.setColor(new BaseColor(206,206,206));
            p1 = new Paragraph("DreamFx(Pvt)Ltd.All rights reserved \u00a9 2019",font);
            p1.setAlignment(Paragraph.ALIGN_CENTER);
            doc.add(p1);

            p1 = new Paragraph("info@dreamfx.lk,0115640640",font);
            p1.setAlignment(Paragraph.ALIGN_CENTER);
            doc.add(p1);*/
            font.setColor(new BaseColor(125,125,126));
            font.setSize(15f);
            Paragraph paragraph = new Paragraph("Download mobile pos at : ",font);
            paragraph.setAlignment(Paragraph.ALIGN_CENTER);
            doc.add(paragraph);


            Font linkFont = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD | Font.UNDERLINE);
            linkFont.setColor(new BaseColor(125,125,126));
            linkFont.setSize(15f);
            //font.setUnderline(0.1f, -2f);
            paragraph = new Paragraph("",font);
            paragraph.setAlignment(Paragraph.ALIGN_CENTER);
            Anchor anchor = new Anchor(
                    "https://play.google.com/store/apps/details?id=com.eclipse.android.salesdrive",linkFont);
            anchor.setReference(
                    "https://play.google.com/store/apps/details?id=com.eclipse.android.salesdrive");
            paragraph.add(anchor);

            doc.add(paragraph);

        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        }
        finally {
            doc.close();

        }

        //
    }


    static String name="";
    static String address="";
    static String number="";
    static String imagePath="";
    static Image image=null;
    private static void addHeader(Document document,Context ctx){
        Cursor infoCur = null;
        DBHelper db=new DBHelper(ctx);

        try {
            String userId = SharedPreference.getInstance().getValue(ctx, Constants.USER_ID);

            infoCur = db.getBussinessDetails(userId);
            while (infoCur.moveToNext()) {
                name = infoCur.getString(infoCur.getColumnIndex((ConstantsUsed.COLUMN_BUSSINESS_NAME)));
                number = infoCur.getString(infoCur.getColumnIndex((ConstantsUsed.COLUMN_BUSSINESS_NUMBER)));
                address = infoCur.getString(infoCur.getColumnIndex((ConstantsUsed.COLUMN_BUSSINESS_ADDRESS)));
                imagePath = infoCur.getString(infoCur.getColumnIndex((ConstantsUsed.COLUMN_BUSSINESS_LOGO)));

                /*TextView tv=findViewById(R.id.email_nav);
                tv.setText("");*/
                if(!imagePath.equalsIgnoreCase("NO_IMAGE") && imagePath!=null) {
                   /* Bitmap bmp=UtilityFunctions.setPic(imagePath);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    image = Image.getInstance(stream.toByteArray());
                    image.scaleToFit(200,100);*/
                }
            }

            //table header

            if(image!=null) {

                PdfPTable table = new PdfPTable(2);
                table.setWidthPercentage(75);
                table.setWidths(new int[]{1, 2});

                PdfPCell cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(createImageCell(imagePath));

                cell = new PdfPCell(createTableCell(name,address,number));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);

                document.add(table);

            }



        }catch (Exception e){
            Toast.makeText(ctx,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }finally{
            if(infoCur!=null)
                infoCur.close();
        }
    }

    public static PdfPTable createTableCell(String name,String address, String number) throws DocumentException, IOException {
        PdfPTable table = new PdfPTable(1);

        Font font = new Font();
        font.setSize(20f);

        float fntSize, lineSpacing;
        fntSize = 20.0f;
        lineSpacing = 10f;
        Paragraph para = new Paragraph(new Phrase(lineSpacing,name,
                FontFactory.getFont(FontFactory.TIMES_BOLD, fntSize)));

        //Paragraph para = new Paragraph(name,font);
        para.setAlignment(Element.ALIGN_CENTER);
        PdfPCell cell = new PdfPCell(para);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPaddingLeft(15);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);

        font.setSize(15f);
        para = new Paragraph(address,font);
        cell = new PdfPCell(new Phrase(para));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPaddingLeft(15);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);

        para = new Paragraph(number,font);
        cell = new PdfPCell(new Phrase(para));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPaddingLeft(15);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);


        return table;
    }


    public static PdfPCell createImageCell(String path) throws DocumentException, IOException {
        PdfPCell cell;
        if(path.equalsIgnoreCase("NO_IMAGE") || path==null){
            cell = new PdfPCell(new Paragraph("test"));
            cell.setBorder(Rectangle.NO_BORDER);
        }else {
            Image img = Image.getInstance(path);
            img.scaleToFit(50, 20);
            cell = new PdfPCell(img, true);
            cell.setBorder(Rectangle.NO_BORDER);
        }
        return cell;
    }
}
