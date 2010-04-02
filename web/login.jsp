<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%String path=request.getContextPath();%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
  <title>Функциональное тестирование WebUI</title>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <link type="image/x-icon" href="<%=path%>/img/favicon.ico" rel="shortcut icon">
  <link type="text/css" href="<%=path%>/resource/theme/resources/css/ext-all.css" rel="stylesheet"/>
  <script type="text/javascript" src="<%=path%>/resource/theme/adapter/ext/ext-base.js"></script>
  <script type="text/javascript" src="<%=path%>/resource/theme/pkgs/ext-foundation.js"></script>
  <script type="text/javascript" src="<%=path%>/resource/theme/pkgs/cmp-foundation.js"></script>
  <script type="text/javascript" src="<%=path%>/resource/theme/pkgs/ext-dd.js"></script>
  <script type="text/javascript" src="<%=path%>/resource/theme/pkgs/pkg-tips.js"></script>
  <script type="text/javascript" src="<%=path%>/resource/theme/pkgs/pkg-buttons.js"></script>
  <script type="text/javascript" src="<%=path%>/resource/theme/pkgs/pkg-toolbars.js"></script>
  <script type="text/javascript" src="<%=path%>/resource/theme/pkgs/pkg-forms.js"></script>
</head>
<body>

<script type="text/javascript">
Ext.onReady(function() {
Ext.BLANK_IMAGE_URL="<%=path%>/resource/theme/resources/images/default/s.gif";
Ext.QuickTips.init();
Ext.namespace("WUI");
WUI.env = {
  location: "<%=path%>",
  theme: "/resource/lib/extjs-debug/",
  version: {major:0,minor:8},
  state: ""
};
WUI.viewport = new Ext.Viewport({
  layout: "hbox",
  layoutConfig: {
    align: "middle",
    pack: "center"
  },
  items: {
    id: "login",
    xtype: "form",
    standardSubmit: true,
    method: "POST",
    url: "<%=path%>/j_security_check",
    title: "Аутентификация пользователя",
    frame: true,
    width: 400,
    height: 120,
    items: [ {
        xtype: "textfield",
        fieldLabel: "Имя",
        anchor: "100%",
        name: "j_username",
        emptyText: "введите имя пользователя...",
        allowBlank: false,
        maxLength: 32
      }, {
        xtype: "textfield",
        fieldLabel: "Пароль",
        anchor: "100%",
        name: "j_password",
        inputType: "password",
        allowBlank: true,
        maxLength: 32
      }
    ],
    fbar: {
      xtype: "toolbar",
      layout: "toolbar",
      buttonAlign: "right",
      items: {
        xtype: "button",
        text: "Войти >>",
        handler: function(b,e) {
          Ext.getCmp("login").getForm().submit();
//          this.ownerCt.ownerCt.getForm().submit();
        }
      }
    }
  }
});
});
</script>

</body>
</html>