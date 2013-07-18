<?php
$file = $_POST['file'];;
exit(md5_file($file));
?>