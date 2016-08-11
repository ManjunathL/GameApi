define(['/js/kapture.js', '/js/mgfirebase.js'], function(Kapture, MGF) {

    return {
        submit: function(name, email, phone, query, floorplan, propertyName, projectName, cityName) {

            Kapture.submit(name, email, phone, query, floorplan, propertyName, projectName, cityName);

            MGF.addConsultData({
                "fullName": name,
                "email": email,
                "contactNumber": phone,
                "requirements": query,
                "propertyName": propertyName,
                "projectName": projectName,
                "cityName": cityName
            });

        }
    };
});