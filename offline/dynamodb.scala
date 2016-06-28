<!DOCTYPE html SYSTEM "about:legacy-compat">
<html manifest="pamflet.manifest">
      <head>
        <meta charset="utf-8"/>
        <meta content="width=device-width, initial-scale=1" name="viewport"/>
        <title>wookie — DynamoDB</title>
        
        <link rel="stylesheet" href="css/blueprint/screen.css" type="text/css" media="screen, projection"/>
        <link rel="stylesheet" href="css/blueprint/grid.css" type="text/css" media="screen and (min-device-width: 800px), projection"/>
        <link rel="stylesheet" href="css/blueprint/print.css" type="text/css" media="print"/> 
        <!--[if lt IE 8]>
          <link rel="stylesheet" href={ relativeBase + "css/blueprint/ie.css" } type="text/css" media="screen, projection"/>
        <![endif]-->
        <link rel="stylesheet" href="css/pamflet.css" type="text/css" media="screen, projection"/>
        <link rel="stylesheet" href="css/pamflet-print.css" type="text/css" media="print"/>
        <link rel="stylesheet" href="css/pamflet-grid.css" type="text/css" media="screen and (min-device-width: 800px), projection"/>
        <link rel="stylesheet" href="css/color_scheme-redmond.css" type="text/css" media="screen, projection"/>
        <link rel="stylesheet" href="css/color_scheme-github.css" type="text/css" media="screen, projection"/>
        <link rel="stylesheet" href="css/color_scheme-monokai.css" type="text/css" media="screen, projection"/>
        <link rel="stylesheet" href="css/pamfletheight_2em_2em.css" type="text/css" media="screen and (min-device-width: 800px), projection"/>
        <script type="text/javascript" src="js/jquery-1.6.2.min.js"></script>
        <script type="text/javascript" src="js/jquery.collapse.js"></script>
        <script type="text/javascript" src="js/pamflet.js"></script>
        <script type="text/javascript">
          Pamflet.page.language = 'en';
        </script>
        <script type="text/javascript" src="js/prettify/prettify.js"></script><script type="text/javascript" src="js/prettify/lang-scala.js"></script><link type="text/css" rel="stylesheet" href="css/prettify.css"/><script type="text/javascript"><!--
        window.onload=function() { prettyPrint(); };
      --></script>
        <link rel="stylesheet" href="css/custom.css" type="text/css" media="screen, projection"/>
        
        
      </head>
      <body class="color_scheme-redmond">
        <a class="page prev nav" href="rationale.html">
            <span class="space">&nbsp;</span>
            <span class="flip arrow">❧</span>
          </a><a class="page next nav" href="Contents+in+Depth.html">
            <span class="space">&nbsp;</span>
            <span class="arrow">❧</span>
          </a>
        <div class="container contentswrapper">
          <div class="span-16 prepend-1 append-1 contents">
            <h1 id="DynamoDB">DynamoDB<a href="#DynamoDB" class="header-link"><span class="header-link-content">&nbsp;</span></a></h1><h2 id="Install">Install<a href="#Install" class="header-link"><span class="header-link-content">&nbsp;</span></a></h2><p>In order to use DynamoDB, you need to add <code>wookie-dynamodb</code> artifact to your
<code>build.sbt</code> file.
</p><pre><code class="">&quot;io.github.pepegar&quot; %% &quot;wookie-dynamodb&quot; % &quot;0.1-SNAPSHOT&quot;
</code></pre><h2 id="Use">Use<a href="#Use" class="header-link"><span class="header-link-content">&nbsp;</span></a></h2><p>You can use the <code>Ops</code> typeclass, located in <code>language</code> package.  It provides 
convenient functions to generate request values for DynamoDB.
</p><pre><code class="prettyprint lang-scala">import wookie.dynamodb.implicits._
import wookie.dynamodb.language.Ops
import wookie.dynamodb.DynamoDB
import wookie.service.Properties
import com.amazonaws.services.dynamodbv2.model.ListTablesRequest
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

val props = Properties(&quot;accessKey&quot;, &quot;secretAccessKey&quot;)
implicit val system = ActorSystem(&quot;wookie&quot;)
implicit val mat = ActorMaterializer()

val listTables = Ops.listTables(new ListTablesRequest)

val result = DynamoDB(props).run(listTables)
</code></pre><div class="bottom nav end span-16">
                        
                      </div><div class="tocwrapper show">
      <a class="tochead nav" style="display: none" href="#toc">❦</a>
      <a name="toc"></a>
      <h4 class="toctitle">Contents</h4>
      <div class="tocbody">
      <div><a href="index.html">wookie</a></div><ol class="toc"> <li><div><a href="rationale.html">Rationale</a></div></li><li><div class="current">DynamoDB</div><ol class="toc">  </ol></li><li class="generated"><div><a href="Contents+in+Depth.html">Contents in Depth</a></div></li><li class="generated"><div><a href="Combined+Pages.html">Combined Pages</a></div></li> </ol></div></div>
          </div>
        </div>
        <div class="header">
          <div class="container">
        <div class="span-16 prepend-1 append-1">
          <div class="span-16 top nav">
            <div class="span-16 title">
              <span>wookie</span> — DynamoDB
            </div>
          </div>
        </div>
      </div>
        </div>
        <div class="footer">
          
        </div>
        
        
      </body>
    </html>