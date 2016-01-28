define(function(){
    return {
        CONSULT_PRE: "consult_",
        addConsultData: function(uid, data) {
            localStorage.setItem(this.CONSULT_PRE + uid, JSON.stringify(data));
        },
        submitAllConsultData: function(submit) {
            for (var i = 0; i < localStorage.length; i++){
                var key = localStorage.key(i);
                if (key.startsWith(this.CONSULT_PRE)) {
                    var consultData = JSON.parse(localStorage.getItem(key));
                    var uid = key.split('_')[1];
                    submit(consultData, uid);
                }
            }
        },
        removeConsultData: function(uid) {
            localStorage.removeItem(this.CONSULT_PRE + uid);
        }
    };
});