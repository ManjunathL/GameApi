define(function() {

    return {
        submit: function(name, email, phone, query, floorplan, propertyName) {

            var formData = new FormData();
            formData.append('name', name);
            formData.append('email', email);
            formData.append('phone', phone);
            query && formData.append('query', query);
            floorplan && formData.append('floorplan', floorplan);
            propertyName && formData.append('consult', propertyName);

            $.ajax({
                /*url: 'http://adjetter.com/lp/orangegubbi-place-an-enquiry.html?auth=bFCAsuGsIdan8f6NdFmv9KGy5W8gW6knGaoNAqt85dU=',*/
                url: restBase + '/api/consult',
                data: formData,
                processData: false,
                contentType: false,
                type: 'POST',
                success: function(data) {
                    console.log(data);
                },
                error: function(error) {
                    console.log(error);
                }
            });
        }
    };
});