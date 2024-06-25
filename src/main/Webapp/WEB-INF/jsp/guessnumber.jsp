<html>
    <head>
        <title>Devinez le nombre</title>
    </head>
    <body>
        <h2>Devinez le nombre entre 1 et 100</h2>
        <c:if test="${not empty message}">
            <h5>${message}</h5>
            <c:if test="${message.startsWith('Félicitations')}">
                <form action="/" method="GET">
                    <input type="submit" value="Nouvelle partie">
                </form>
            </c:if>
        </c:if>
        <form action="/" method="post">
            <label for="number">Entrez votre nombre : </label>
            <input type="number"  name="number"> <input type="submit" value="Devinez">
        </form>
    </body>
</html>



