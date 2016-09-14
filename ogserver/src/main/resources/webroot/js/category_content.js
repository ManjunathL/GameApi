define([
    'jquery',
    'underscore',
    'text!templates/category/bedroom.html',
    'text!templates/category/book_rack.html',
    'text!templates/category/crockery_unit.html',
    'text!templates/category/entertainment_unit.html',
    'text!templates/category/foyer_unit.html',
    'text!templates/category/kitchen.html',
    'text!templates/category/l_shaped_kitchen.html',
    'text!templates/category/living_&_dining.html',
    'text!templates/category/parallel_kitchen.html',
    'text!templates/category/shoe_rack.html',
    'text!templates/category/side_table.html',
    'text!templates/category/sideboard.html',
    'text!templates/category/straight_kitchen.html',
    'text!templates/category/study_table.html',
    'text!templates/category/u_shaped_kitchen.html',
    'text!templates/category/wardrobe.html'
], function($, _, Bedroom, BookRack, CrockeryUnit, EntertainmentUnit, FoyerUnit, Kitchen, LShapedKitchen, LivingAndDining,
    ParallelKitchen, ShoeRack, SideTable, Sideboard, StraightKitchen, StudyTable, UShapedKitchen, Wardrobe) {
    return {
        el: '.category-content',
        apply: function(category) {

            var pageContent;

            switch (category) {
                case "bedroom": pageContent = Bedroom; break;
                case "Book Rack": pageContent = BookRack; break;
                case "Crockery Unit": pageContent = CrockeryUnit; break;
                case "Entertainment Unit": pageContent = EntertainmentUnit; break;
                case "Foyer Unit": pageContent = FoyerUnit; break;
                case "kitchen": pageContent = Kitchen; break;
                case "L Shaped Kitchen": pageContent = LShapedKitchen; break;
                case "living & dining": pageContent = LivingAndDining; break;
                case "Parallel Kitchen": pageContent = ParallelKitchen; break;
                case "Shoe Rack": pageContent = ShoeRack; break;
                case "Side Table": pageContent = SideTable; break;
                case "Sideboard": pageContent = Sideboard; break;
                case "Straight Kitchen": pageContent = StraightKitchen; break;
                case "Study Table": pageContent = StudyTable; break;
                case "U Shaped Kitchen": pageContent = UShapedKitchen; break;
                case "Wardrobe": pageContent = Wardrobe; break;
            }

            if (pageContent !== null && pageContent !== '') {
                    $(this.el).html(pageContent);
            }
        }
    };
});