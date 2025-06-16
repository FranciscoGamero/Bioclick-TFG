package com.salesianostriana.bioclick.util;



import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import lombok.extern.java.Log;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Log
@Service
public class SendGridMailSender {

    @Value("${sendgrid.api.key}")
    private String sendgridApiKey;


    @Async
public void sendMail(String to, String subject, String code) throws IOException {
    Email from = new Email("contactobioclick@gmail.com");
    Email emailTo = new Email(to);

    String htmlContent = """
    <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
    <html data-editor-version="2" class="sg-campaigns" xmlns="http://www.w3.org/1999/xhtml">
    <head>
      <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
      <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1">
      <meta http-equiv="X-UA-Compatible" content="IE=Edge">
      <style type="text/css">
        body, p, div { font-family: inherit; font-size: 14px; }
        body { color: #000000; }
        body a { color: #1188E6; text-decoration: none; }
        p { margin: 0; padding: 0; }
        table.wrapper { width:100%% !important; table-layout: fixed; }
        img.max-width { max-width: 100%% !important; }
        @media screen and (max-width:480px) {
          .columns { width: 100%% !important; }
          .column { display: block !important; width: 100%% !important; }
        }
      </style>
      <link href="https://fonts.googleapis.com/css?family=Muli&display=swap" rel="stylesheet">
      <style>body {font-family: 'Muli', sans-serif;}</style>
    </head>
    <body>
      <center class="wrapper" data-link-color="#1188E6" data-body-style="font-size:14px; font-family:inherit; color:#000000; background-color:#FFFFFF;">
        <div class="webkit">
          <table cellpadding="0" cellspacing="0" border="0" width="100%%" class="wrapper" bgcolor="#FFFFFF">
            <tr>
              <td valign="top" bgcolor="#FFFFFF" width="100%%">
                <table width="100%%" role="content-container" class="outer" align="center" cellpadding="0" cellspacing="0" border="0">
                  <tr>
                    <td width="100%%">
                      <table width="100%%" cellpadding="0" cellspacing="0" border="0">
                        <tr>
                          <td>
                            <table width="100%%" cellpadding="0" cellspacing="0" border="0" style="width:100%%; max-width:600px;" align="center">
                              <tr>
                                <td role="modules-container" style="padding:0px 0px 0px 0px; color:#000000; text-align:left;" bgcolor="#FFFFFF" width="100%%" align="left">
                                  <table border="0" cellpadding="0" cellspacing="0" align="center" width="100%%" role="module" data-type="columns" style="padding:30px 20px 30px 20px;" bgcolor="#f6f6f6" data-distribution="1">
                                    <tbody>
                                      <tr role="module-content">
                                        <td height="100%%" valign="top">
                                          <table width="540" style="width:540px; border-spacing:0; border-collapse:collapse; margin:0px 10px 0px 10px;" cellpadding="0" cellspacing="0" align="left" border="0" bgcolor="" class="column column-0">
                                            <tbody>
                                              <tr>
                                                <td style="padding:0px;margin:0px;border-spacing:0;">
                                                  <table class="module" role="module" data-type="spacer" border="0" cellpadding="0" cellspacing="0" width="100%%" style="table-layout: fixed;">
                                                    <tbody>
                                                      <tr>
                                                        <td style="padding:0px 0px 20px 0px;" role="module-content" bgcolor=""></td>
                                                      </tr>
                                                    </tbody>
                                                  </table>
                                                  <table class="wrapper" role="module" data-type="image" border="0" cellpadding="0" cellspacing="0" width="100%%" style="table-layout: fixed;">
                                                    <tbody>
                                                      <tr>
                                                        <td style="font-size:6px; line-height:10px; padding:0px 0px 0px 0px;" valign="top" align="center">
                                                          <img class="max-width" border="0" style="display:block; color:#000000; text-decoration:none; font-family:Helvetica, arial, sans-serif; font-size:16px;" width="251" alt="" data-proportionally-constrained="true" data-responsive="false" src="http://cdn.mcauto-images-production.sendgrid.net/71ddc1bd82de96ec/75fc3da8-a5de-4e9d-a84e-3e44a4dadb9d/2564x1022.png" height="100">
                                                        </td>
                                                      </tr>
                                                    </tbody>
                                                  </table>
                                                  <table class="module" role="module" data-type="spacer" border="0" cellpadding="0" cellspacing="0" width="100%%" style="table-layout: fixed;">
                                                    <tbody>
                                                      <tr>
                                                        <td style="padding:0px 0px 30px 0px;" role="module-content" bgcolor=""></td>
                                                      </tr>
                                                    </tbody>
                                                  </table>
                                                  <table class="module" role="module" data-type="text" border="0" cellpadding="0" cellspacing="0" width="100%%" style="table-layout: fixed;">
                                                    <tbody>
                                                      <tr>
                                                        <td style="padding:50px 30px 18px 30px; line-height:36px; text-align:inherit; background-color:#ffffff;" height="100%%" valign="top" bgcolor="#ffffff" role="module-content">
                                                        <div style="font-family: inherit; text-align: center">
                                                            <span style="color:rgb(0, 145, 31); font-family: Colfax, Helvetica, Arial, sans-serif; font-size: 18px;">
                                                                Para verificar su cuenta, introduzca el siguiente código:
                                                            </span>
                                                        </div>
                                                        <div style="text-align: center; margin: 30px 0;">
                                                            <span style="font-size: 32px; font-weight: bold; color:rgb(0, 0, 0);">%s</span>
                                                        </div>
                                                        </td>
                                                      </tr>
                                                    </tbody>
                                                  </table>
                                                </td>
                                              </tr>
                                            </tbody>
                                          </table>
                                        </td>
                                      </tr>
                                    </tbody>
                                  </table>
                                  <div data-role="module-unsubscribe" class="module" role="module" data-type="unsubscribe" style="color:#444444; font-size:12px; line-height:20px; padding:16px 16px 16px 16px; text-align:Center;">
                                    <p style="font-size:12px; line-height:20px;">
                                      <a target="_blank" class="Unsubscribe--unsubscribeLink zzzzzzz" href="{{{unsubscribe}}}" style="">Unsubscribe</a> - 
                                      <a href="{{{unsubscribe_preferences}}}" target="_blank" class="Unsubscribe--unsubscribePreferences" style="">Unsubscribe Preferences</a>
                                    </p>
                                  </div>
                                </td>
                              </tr>
                            </table>
                          </td>
                        </tr>
                      </table>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
          </table>
        </div>
      </center>
    </body>
    </html>
    """;

    htmlContent = String.format(htmlContent, code);

    Content content = new Content("text/html", htmlContent);
    Mail mail = new Mail(from, subject, emailTo, content);

    SendGrid sg = new SendGrid(sendgridApiKey);
    Request request = new Request();
    try {
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        Response response = sg.api(request);
        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());
        System.out.println(response.getHeaders());
    } catch (IOException ex) {
        throw ex;
    }
}
}
