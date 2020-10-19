package com.prolog.eis.controller.yqfs;

import com.prolog.eis.dao.order.BillFailingMapper;
import com.prolog.eis.dto.outbound.YkBalingBox;
import com.prolog.eis.model.order.BillFailing;
import com.prolog.framework.common.message.RestMessage;
import com.prolog.framework.core.restriction.Criteria;
import com.prolog.framework.core.restriction.Restrictions;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author LuoLi
 * @version 1.0
 * @date 2020/8/8 11:00
 */

@RestController
@RequestMapping("/api/v1/master/excel")
public class PrologExcelController {

    @Autowired
    private BillFailingMapper billFailingMapper;

    //移库绑定接口
    @PostMapping("ykbaling")
    public RestMessage<List<YkBalingBox>> ykDownload2() throws IOException {

        List<YkBalingBox> ykBalingBox = billFailingMapper.getYkBalingBox();
        return RestMessage.newInstance(true,"成功",ykBalingBox) ;

    }

    //合单失败接口
    @PostMapping("billfailing")
    public RestMessage<List<BillFailing>> billDownload2() throws IOException {


        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, -2);
        Date date = c.getTime();

        Criteria criteria = new Criteria(BillFailing.class);
        criteria.setRestriction(Restrictions.between("createTime", date, new Date()));
        List<BillFailing> billFailings = billFailingMapper.findByCriteria(criteria);
        return RestMessage.newInstance(true,"成功",billFailings);

    }


    @RequestMapping("/bill/download")
    @Deprecated
    public void billDownload(HttpServletResponse response) throws IOException {

        String[] header = {"编号", "清单号", "波次", "经销商id", "商品编号", "商品名称", "计划数量", "时间"};

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, -2);
        Date date = c.getTime();

        Criteria criteria = new Criteria(BillFailing.class);
        criteria.setRestriction(Restrictions.between("createTime", date, new Date()));
        List<BillFailing> billFailings = billFailingMapper.findByCriteria(criteria);

        HSSFWorkbook workbook = new HSSFWorkbook();

        HSSFSheet sheet = workbook.createSheet("合单失败明细");
        sheet.setDefaultColumnWidth(15);

        HSSFRow headrow = sheet.createRow(0);
        for (int i = 0; i < header.length; i++) {
            HSSFCell cell = headrow.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(header[i]);
            cell.setCellValue(text);
        }

        for (int i = 1; i < billFailings.size() + 1; i++) {

            HSSFRow row = sheet.createRow(i);

            HSSFCell cell0 = row.createCell(0);
            HSSFRichTextString text0 = new HSSFRichTextString(i + "");
            cell0.setCellValue(text0);
            int x = i - 1;
            HSSFCell cell1 = row.createCell(1);
            HSSFRichTextString text1 = new HSSFRichTextString(billFailings.get(x).getBillNo());
            cell1.setCellValue(text1);

            HSSFCell cell2 = row.createCell(2);
            HSSFRichTextString text2 = new HSSFRichTextString(billFailings.get(x).getWaveNo());
            cell2.setCellValue(text2);

            HSSFCell cell3 = row.createCell(3);
            HSSFRichTextString text3 = new HSSFRichTextString(billFailings.get(x).getDealerId() + "");
            cell3.setCellValue(text3);

            HSSFCell cell4 = row.createCell(4);
            HSSFRichTextString text4 = new HSSFRichTextString(billFailings.get(x).getGoodsNo());
            cell4.setCellValue(text4);

            HSSFCell cell5 = row.createCell(5);
            HSSFRichTextString text5 = new HSSFRichTextString(billFailings.get(x).getGoodsName());
            cell5.setCellValue(text5);

            HSSFCell cell6 = row.createCell(6);
            HSSFRichTextString text6 = new HSSFRichTextString(billFailings.get(x).getPlanNum() + "");
            cell6.setCellValue(text6);

            HSSFCell cell7 = row.createCell(7);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date createTime = billFailings.get(x).getCreateTime();
            HSSFRichTextString text7 = new HSSFRichTextString(simpleDateFormat.format(createTime));
            cell7.setCellValue(text7);

        }

        response.setContentType("application/octet-stream");

        String fileName = "合单失败明细";
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + new String(fileName.getBytes("utf-8"), "ISO-8859-1") + ".xls");

        response.flushBuffer();

        workbook.write(response.getOutputStream());
    }

    @RequestMapping("/yk/download")
    public void ykDownload(HttpServletResponse response) throws IOException {

        String[] header = {"编号", "清单号", "名称", "商品编号", "数量", "订单箱号", "打包箱号", "创建时间"};
        HSSFWorkbook workbook = new HSSFWorkbook();
        List<YkBalingBox> ykBalingBox = billFailingMapper.getYkBalingBox();
        HSSFSheet sheet = workbook.createSheet("移库");
        sheet.setDefaultColumnWidth(15);
        HSSFRow headrow = sheet.createRow(0);
        for (int i = 0; i < header.length; i++) {
            HSSFCell cell = headrow.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(header[i]);
            cell.setCellValue(text);
        }

        for (int i = 1; i < ykBalingBox.size() + 1; i++) {
            HSSFRow row = sheet.createRow(i);

            HSSFCell cell0 = row.createCell(0);
            HSSFRichTextString text0 = new HSSFRichTextString(i + "");
            cell0.setCellValue(text0);
            int x = i - 1;
            HSSFCell cell1 = row.createCell(1);
            HSSFRichTextString text1 = new HSSFRichTextString(ykBalingBox.get(x).getBillNo());
            cell1.setCellValue(text1);
            HSSFCell cell2 = row.createCell(2);
            HSSFRichTextString text2 = new HSSFRichTextString(ykBalingBox.get(x).getGoodsName());
            cell2.setCellValue(text2);
            HSSFCell cell3 = row.createCell(3);
            HSSFRichTextString text3 = new HSSFRichTextString(ykBalingBox.get(x).getGoodsNo());
            cell3.setCellValue(text3);
            HSSFCell cell4 = row.createCell(4);
            HSSFRichTextString text4 = new HSSFRichTextString(ykBalingBox.get(x).getCommodityNum() + "");
            cell4.setCellValue(text4);
            HSSFCell cell5 = row.createCell(5);
            HSSFRichTextString text5 = new HSSFRichTextString(ykBalingBox.get(x).getOrderBoxNo());
            cell5.setCellValue(text5);
            HSSFCell cell6 = row.createCell(6);
            HSSFRichTextString text6 = new HSSFRichTextString(ykBalingBox.get(x).getBalingBoxNo());
            cell6.setCellValue(text6);
            HSSFCell cell7 = row.createCell(7);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date createTime = ykBalingBox.get(x).getCreateTime();
            HSSFRichTextString text7 = new HSSFRichTextString(simpleDateFormat.format(createTime));
            cell7.setCellValue(text7);
            response.setContentType("application/octet-stream");

            String fileName = "移库";
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + new String(fileName.getBytes("utf-8"), "ISO-8859-1") + ".xls");

            response.flushBuffer();

            workbook.write(response.getOutputStream());

        }


    }

}
