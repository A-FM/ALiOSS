var OSS = OSS.Wrapper;
var key;
var FileUpload = function () {
    var appServer = 'http://localhost:8080/file/upload';
    var region = 'oss-cn-beijing';


    var applyTokenDo = function (func, bucketName,folder,fileId) {

        return $.ajax({
            url: appServer,
            type: "GET",
            dataType: "JSON",
            async: false,
            success: function (result) {
                var client = new OSS({
                    region: region,
                    accessKeyId: result.accessKeyId,
                    accessKeySecret: result.accessKeySecret,
                    stsToken: result.securityToken,
                    bucket: bucketName
                });
                return func(client,folder,fileId);
            }
        });
    };

    var progress = function (p) {
        return function (done) {
            console.log("上传进度: " + Math.floor(p * 100) + '%');
            done();
        }
    };

    var uploadFile = function (client,folder,fileId) {

        // 文件名生成
        var file = document.getElementById(fileId).files[0];

        var key = determineFileTypeAndReturnFileName(file,folder);

        return client.multipartUpload(key, file, {
            progress: progress
        }).then(function (data) {
            console.log("上传成功!");
            return data;
        });

    };

    // 确定文件类型并返回文件名.
    var determineFileTypeAndReturnFileName = function(file,folder){
        // 文件名生成
        var fileNameList = file.name.split(".");
        key = folder + "/" + (new Date()).getTime() + "-" + String(Math.random()).split(".")[1] + "." + fileNameList[fileNameList.length - 1];
        return key;
    };

    return {
        /**
         * @param bucketName 仓库名
         * @param folder 文件的所属文件夹, 可以自动创建
         * @param fileId 前端标签ID
         */
        upload: function(bucketName,folder,fileId){
            console.log(bucketName);
            applyTokenDo(uploadFile,bucketName,folder,fileId);
            return key;
        },
    }
}();