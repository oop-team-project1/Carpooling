let activePage = 0
const getFeedBacks = async () => {

    const userId = window.location.href.split('/').at(-1)

    const response = await
    fetch(`http://localhost:8080/roadbuddy/api/v1/users/${userId}/feedbacks`)

    const data = await response.json()

    return data
}

const createFeedBacks = async () => {
    const feedBacks = await getFeedBacks()
    let html = ''

  
    feedBacks.slice(activePage * 3, activePage *  3 + 3).forEach(feedback => {
        const {dateOfCreation, rating} = feedback
        const {firstName, lastName} = feedback?.creator
        const content = feedback?.feedbackComment?.content


         html += `<li id="user-feedback">
          <figure>
              <img src="/images/resources/reviewer-3.jpg" alt="">
          </figure>
          <div class="activity-meta">
             <div class="user-rating">
                <span>
             <a href="#" title="">${firstName} ${lastName}</a>
            </span>
            <span id="date">${dateOfCreation}</span>
              </div>
              <ul class="stars-rating">
                  <div class="stars-outer"
                       ${rating}
                     ><div class="stars-inner" style="width: ${rating * 20}%"></div>
                  </div>
              </ul>
              <p>
                 ${content ? content : ''}
              </p>
          </div>
      </li>`
    });


    return html
}

const createPagination = (pages) => {
    let html = '';
    for(let i=0;i<pages;i++) {
        html+= `<div class="pagination-page" id="fb-page-button" data-value="${i}">${i + 1}</div>`
    }
    document.getElementById('fb-pagination').innerHTML = html


}

const pagination = async () => {
    const feedBacks = await getFeedBacks()


    let html = await createFeedBacks();
    createPagination( Math.ceil(feedBacks.length / 3))

    document.getElementById('reviewer-list').innerHTML = html;

};

document.getElementById('fb-pagination').addEventListener('click', (e) => {
    activePage = e.target.dataset.value
    pagination()
})

pagination()

