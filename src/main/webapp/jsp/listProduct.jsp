<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <!-- CSS  -->
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
        <link href="https://code.jquery.com/ui/1.11.1/themes/smoothness/jquery-ui.css" rel="stylesheet"/>
        <link href="css/materialize.css" type="text/css" rel="stylesheet" media="screen,projection"/>
        <link href="css/style.css" type="text/css" rel="stylesheet" media="screen,projection"/>
        <title>BUSCU</title>
    </head>

    <body>
        <nav class="light-teal-team2 lighten-1" role="navigation">
            <div class="nav-wrapper container">
                <a id="logo-container" href="#" class="brand-logo">BUSCU</a>
            </div>
        </nav>

        <%--<!-- IMPORT FILE -->
        <div class="section no-pad-bot" id="index-banner">
            <div class="container">
                <br>
                <div class="row">
                    <form:form method="POST" commandName="csvFile" action="upload" enctype="multipart/form-data" onsubmit="myFunction()" id="form">
                        <div class="file-field input-field">
                            <div class="btn">
                                <span>File<i class="material-icons right">description</i></span>
                                <input required type="file" name="file" data-icon="false" id="file">
                            </div>
                            <div class="file-path-wrapper">
                                <input class="file-path validate" type="text">
                            </div>
                        </div>
                    <div class="center">
                        <button class="btn waves-effect waves-light" type="submit" name="action" id ="submitBtn">
                            Submit <i class="material-icons right">send</i>
                        </button>
                    </div>
                    </form:form>
                </div>
                <br>
            </div>
        </div>--%>

        <!-- IMPORT FILE -->
        <div class="section no-pad-bot" id="index-banner">
            <div class="container">
                    <form:form method="GET"  action="process" id="form">
                        <div class="input-field">
                            <div class="input">
                                <input type="text" name="fromPage" id="fromPage" required="true">
                                <br>
                                <input type="text" name="toPage" id="toPage" required="true">
                            </div>
                        </div>
                        <div class="center">
                            <button class="btn waves-effect waves-light" type="submit" name="action" id ="submitBtn">
                                Process <i class="material-icons right">send</i>
                            </button>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>

        <!-- PRELOADER -->
        <div class="container" id="progress" style="visibility: hidden">
            <div class="progress">
                <div class="indeterminate"></div>
            </div>
        </div>

        <!-- MESSAGE -->
        <div class="container">
            <div class="section">
                <div class="row">
                    <c:forEach var="error" items="${errorExceptionList}">
                        <h5 class="left red-text">${error.errMsg}</h5>
                    </c:forEach>
                </div>
            </div>
        </div>

        <!-- BUTTON SHOW PRODUCT LIST ERROR -->
        <c:if test="${productErrorList.size() > 0}">
        <div class="container">
            <div class="section">
                <a href="#popup" class="open-popup-link">Show List Product Error</a>
            </div>
        </div>
        </c:if>

        <div id="popup">
            <table id="table0" class="tablesorter">
                <caption>Show List Product Error When Insert</caption>
                <thead>
                <tr>
                    <th>Product Code</th>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>price</th>
                    <th>Date Of Birth</th>
                    <th>image1</th>
                    <th>image2</th>
                </tr>
                </thead>
                <tbody>
                <tbody>
                <c:forEach var="product" items="${productErrorList}">
                    <tr>
                        <td>${product.productCode}</td>
                        <td>${product.branchName}</td>
                        <td>${product.title}</td>
                        <c:choose>
                            <c:when test="${product.price == 1}">
                                <td>Male</td>
                            </c:when>
                            <c:otherwise>
                                <td>Female</td>
                            </c:otherwise>
                        </c:choose>
                        <td>${product.saleRank}</td>
                        <td>${product.image1}</td>
                        <td>${product.image2}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

        <c:if test="${pagingProductList.size() > 0}">
        <!-- PAGING -->
        <div class="container">
            <div class="section">
                <div class="right">
                    <ul class="pagination">
                        <%--For displaying Previous link except for the 1st page --%>
                        <c:if test="${currentPage != 1}">
                            <li><a href="listProduct?page=${currentPage - 1}">Previous</a></li>
                            <li class="disabled"><a href="#!"><i class="material-icons">chevron_left</i></a></li>
                        </c:if>

                        <%--For displaying Page numbers.
                        The when condition does not display a link for the current page--%>
                        <c:forEach begin="1" end="${noOfPages}" var="i">
                            <c:choose>
                                <c:when test="${currentPage == i}">
                                <li class="active"><a href="#!">${i}</a></li>
                                </c:when>
                                <c:otherwise>
                                <li class="waves-effect"><a href="listProduct?page=${i}">${i}</a></li>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>

                        <%--For displaying Next link --%>
                        <c:if test="${currentPage < noOfPages}">
                            <li class="disabled"><a href="#!"><i class="material-icons">chevron_right</i></a></li>
                            <li><a href="listProduct?page=${currentPage + 1}">Next</a></li>
                        </c:if>
                    </ul>
                </div>
            </div>
        </div>
        <br><br>

        <!-- PRODUCT LIST -->
        <div class="container">
            <div class="section">
                <h4 class="teal-text text-darken-2">Product List</h4>
                <hr/>
                <div class="row">
                    <table class="bordered highlight responsive-table">
                        <thead>
                        <tr>
                            <td>Product Code</td>
                            <td>Last Name</td>
                            <td>First Name</td>
                            <td>Sex</td>
                            <td>Date Of Birth</td>
                            <td>Phone Number</td>
                            <td>image2</td>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="product" items="${pagingProductList}">
                            <tr>
                                <td>${product.productCode}</td>
                                <td>${product.branchName}</td>
                                <td>${product.title}</td>
                                <c:choose>
                                    <c:when test="${product.price == 1}">
                                        <td>Male</td>
                                    </c:when>
                                    <c:otherwise>
                                        <td>Female</td>
                                    </c:otherwise>
                                </c:choose>
                                <td>${product.saleRank}</td>
                                <td>${product.image1}</td>
                                <td>${product.image2}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <br><br>
        <footer class="page-footer">
            <div class="footer-copyright">
                <div class="container">
                    Create by Team 02
                </div>
            </div>
        </footer>
        </c:if>

        <!--  Scripts-->
        <script src="http://code.jquery.com/jquery-1.11.1.min.js"></script>
        <script src="http://code.jquery.com/ui/1.11.1/jquery-ui.min.js"></script>
        <script src="js/materialize.js"></script>
        <script src="js/init.js"></script>
        <script type="text/javascript">
            function myFunction() {
                document.getElementById("submitBtn").disabled = true;
                document.getElementById("progress").style.visibility="visible";
                document.getElementById("message").style.visibility="visible";

                $.ajax({
                    type: "POST",
                    dataType: formdata,
                    url: "${home}/upload",
                    data: $("#form").serialize(), //serializes the form's elements.
                    success: function(data)
                    {
                        alert('A success');// show response from the php script.
                    }
                  });
              }

            $(function () {
                $('.open-popup-link').click(function () {
                    dialog.dialog("open");
                });
                var dialog = $("#popup").dialog({
                    autoOpen: false,
                    height: $(window).height() - 20,
                    width: 1024,
                    modal: true,
                    open: function (event, ui) {
                        // Will fire when this popup is opened
                        // jQuery UI Dialog widget
                        $('#table0').tablesorter({
                            theme: 'blue',
                            headerTemplate: '{content} {icon}', // Add icon for various themes
                            widgets: ['zebra', 'filter', 'stickyHeaders'],
                            widgetOptions: {
                                // jQuery selector or object to attach sticky header to
                                stickyHeaders_attachTo: '#popup',
                                stickyHeaders_offset: 0,
                                stickyHeaders_addCaption: true
                            }
                        });
                    }
                });
            });
        </script>
    </body>
</html>
