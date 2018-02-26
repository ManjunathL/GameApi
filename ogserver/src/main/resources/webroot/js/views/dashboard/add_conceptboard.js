define([
    'jquery',
    'underscore',
    'text!templates/dashboard/add_conceptboard.html',
    'collections/spacetypelists',
    'collections/spacetemplates'
], function($, _, AddConceptboardPageTemplate, SpaceTypeLists, SpaceTemplates) {
    return {
        el: '#addcboard-dtls',
        spacetypelists: null,
        spacetemplates: null,
        apply: function() {
            this.spacetypelists = new SpaceTypeLists();
            this.spacetemplates = new SpaceTemplates();
            var that = this;
            var spaceTypeCode = '';
            var getSpaceTypeListPromise = that.getSpaceTypeList();
            var viewSpaceTemplates = that.viewSpaceTemplates();

            Promise.all([getSpaceTypeListPromise, viewSpaceTemplates]).then(function() {
                console.log("@@@@@@@@@@@@@ In side Promiseeeeeeeeeeeeee @@@@@@@@@@@@@@@@@@");
                that.fetchSpacetypesAndRender(spaceTypeCode);
            });
        },
        getSpaceTypeList: function(){
            var that = this;
            return new Promise(function(resolve, reject) {
                 if (!that.spacetypelists.get('id')) {
                    that.spacetypelists.fetch({
                        async: true,
                        crossDomain: true,
                        method: "GET",
                        headers:{
                         "authorization": "Bearer "+ sessionStorage.authtoken
                        },
                        success: function(response) {
                            console.log(" +++++++++++++++ Space Type Lists++++++++++++++++++ ");
                            console.log(response);
                            resolve();
                        },
                        error: function(model, response, options) {
                          reject();
                        }
                    });
                 }else{
                    resolve();
                 }
            });
        },
        viewSpaceTemplates: function(spaceTypeCode){
            var that = this;
            console.log(" +++++++++++++++ Space Type Templates++++++++++++++++++ ");
            console.log(spaceTypeCode);
            if(typeof(spaceTypeCode) == 'undefined'){
                var spaceTypeCode = 'SP-ENTRY';
            }

            that.spacetemplates.getSpaceTemplateList(spaceTypeCode,{
                async: true,
                crossDomain: true,
                method: "GET",
                headers:{
                 "authorization": "Bearer "+ sessionStorage.authtoken
                },
                success: function(response) {
                    console.log(" +++++++++++++++ Space Type Templates++++++++++++++++++ ");
                    console.log(response);
                    that.fetchSpacetypesAndRender(spaceTypeCode);
                    //resolve();
                },
                error: function(model, response, options) {
                  //reject();
                  console.log(" +++++++++++++++ Space Type Templates Error ++++++++++++++++++ ");
                                      console.log(response);
                }
            });

        },
        fetchSpacetypesAndRender: function(spaceTypeCode){
            var that = this;
            var spacetypelists = that.spacetypelists;
            var spacetemplates = that.spacetemplates;

            if(typeof(spaceTypeCode) == 'undefined'){
                spaceTypeCode = '';
            }

            $(this.el).html(_.template(AddConceptboardPageTemplate)({
               "spacetypelists": spacetypelists.toJSON(),
               "spacetemplates": spacetemplates.toJSON(),
               "selectedspaceTypeCode": spaceTypeCode
           }));
        }
    };
});